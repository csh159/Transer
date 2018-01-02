package com.scott.annotionprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
public class TaskEventAnnotionProcessor extends AbstractProcessor {

    public static final String CLAZZ_EXT = "$$TaskEventScriber";
    public static final String SCRIBER_NAME = "mScriber";
    private Map<String,Map<String,TaskSubcriberParams>> mScriberInfo = new HashMap<>();
    private Filer mFiler;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        getTaskScribeerInfo(roundEnv);
        generateCode();
        return false;
    }

    private void getTaskScribeerInfo(RoundEnvironment roundEnv) {
        //get all method element whitch indentify the Subcribe annotion
        for (Element element : roundEnv.getElementsAnnotatedWith(TaskSubscriber.class)) {

            //filter the other type element
            if (element.getKind() == ElementKind.METHOD) {
                //get the class which the method belong to.
                Element typeClass = element.getEnclosingElement();
                //method element
                ExecutableElement execEle = (ExecutableElement) element;
                //method params != 1. is not a right event perform method.
                String clazzName = ((TypeElement)typeClass).getQualifiedName().toString();
                //save the method info,className -> method info (EventObserverProxy)
                Map<String,TaskSubcriberParams> method = null;
                if (mScriberInfo.get(clazzName) == null) {
                    method = new HashMap<>();
                } else {
                    method = mScriberInfo.get(clazzName);
                }
                if(element == null || element.getSimpleName() == null) continue;
                String methodName = element.getSimpleName().toString();
                TaskSubscriber subscribe = element.getAnnotation(TaskSubscriber.class);

                //System.out.println("============" + execEle.getParameters().get(0).asType().toString());
                TaskSubcriberParams params = new TaskSubcriberParams();
                params.processType = subscribe.processType();
                params.taskType = subscribe.taskType();
                params.threadMode = subscribe.threadMode();
                params.hasExtas = execEle.getParameters() != null && !execEle.getParameters().isEmpty();

                method.put(methodName, params);

                if (!mScriberInfo.keySet().contains(clazzName)) {
                    mScriberInfo.put(clazzName, method);
                }
            }
        }
    }

    private void generateCode() {
        for (String clazzName : mScriberInfo.keySet()) {
            //System.out.println("clazz == " + clazzName);

            String simpleName = clazzName.substring(clazzName.lastIndexOf(".") + 1,clazzName.length());
            String packageName = clazzName.substring(0,clazzName.lastIndexOf("."));
            TypeSpec.Builder builder = TypeSpec.classBuilder(simpleName + CLAZZ_EXT)
                    .addField(TypeName.OBJECT,SCRIBER_NAME,Modifier.PUBLIC)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ITaskEventDispatcher.class);

            Map<String,TaskSubcriberParams> params = mScriberInfo.get(clazzName);
            Map<TaskType,List<Map<String,TaskSubcriberParams>>> taskTypeMethods = new HashMap<>();
            Map<ProcessType,List<Map<String,TaskSubcriberParams>>> processMethods = new HashMap<>();

            for(String menthodName : params.keySet()) {
                TaskSubcriberParams params1 = params.get(menthodName);
                List<Map<String,TaskSubcriberParams>> sameType = taskTypeMethods.get(params1.taskType);
                if(sameType == null) {
                    sameType = new ArrayList<>();
                    taskTypeMethods.put(params1.taskType,sameType);
                }

                Map<String,TaskSubcriberParams> methodParam = new HashMap<>();
                methodParam.put(menthodName,params1);
                sameType.add(methodParam);

                for(ProcessType processType : params1.processType) {
                    System.out.println("m = " + menthodName + ",paramType = " + Arrays.asList(params1.processType));
                    sameType = processMethods.get(processType);
                    if (sameType == null) {
                        sameType = new ArrayList<>();
                        processMethods.put(processType, sameType);
                    }

                    methodParam = new HashMap<>();
                    methodParam.put(menthodName,params1);
                    sameType.add(methodParam);
                }
            }
            System.out.println("=============" + taskTypeMethods);
            System.out.println("=============" + processMethods);
//
//
            Class<ITaskEventDispatcher> eventClz = ITaskEventDispatcher.class;
            Method[] decalaredMethods = eventClz.getMethods();
            final String interfaceMethodName = decalaredMethods[0].getName();

            MethodSpec.Builder method = MethodSpec.methodBuilder(interfaceMethodName)
                    .addAnnotation(Override.class)
                    .addParameter(TaskType.class,"taskType")
                    .addParameter(ProcessType.class,"processType")
                    .addParameter(List.class,"tasks")
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(generateCurTypeCode(taskTypeMethods));
            builder.addMethod(method.build());

            for(TaskType type : TaskType.values()) {
                method = MethodSpec.methodBuilder("On" + type.name())
                        .addParameter(ProcessType.class, "processType")
                        .addParameter(List.class, "tasks",Modifier.FINAL)
                        .addModifiers(Modifier.PRIVATE)
                        .addCode(createTypeSwitch(processMethods,type,clazzName));
                builder.addMethod(method.build());
            }

            TypeSpec type = builder.build();

            try {
                JavaFile javaFile = JavaFile.builder(packageName, type)
                        .build();
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String generateCurTypeCode(Map<TaskType,List<Map<String,TaskSubcriberParams>>> taskTypeMethods) {
        StringBuilder sb = new StringBuilder();
        List<Map<String,TaskSubcriberParams>> params = null;
        sb.append("switch(taskType) {\n");
        for(TaskType taskType : taskTypeMethods.keySet()) {
            sb.append("    case " + taskType + ": \n");
            sb.append("    On" + taskType + "(processType,tasks);\n");
            sb.append("    break;\n");

        }
        sb.append("}");
        return sb.toString();
    }

    private String createTypeSwitch(Map<ProcessType,List<Map<String,TaskSubcriberParams>>> processMethods,TaskType type,String clazzName) {
        StringBuilder sb = new StringBuilder();
        sb.append("com.scott.transer.event.ITaskPoster taskPoster = null;\n");
        sb.append("switch(processType) {\n");
        for(ProcessType processType : processMethods.keySet()) {
            sb.append("case " + processType + ":\n");
            List<Map<String,TaskSubcriberParams>> params = processMethods.get(processType);
            for(Map<String,TaskSubcriberParams> param : params){
                for(String methodName : param.keySet()) {
                    TaskSubcriberParams methodParam = param.get(methodName);
                    if(methodParam.taskType == type) {
                        sb.append("taskPoster = com.scott.transer.event.TaskEventBus.getDefault().getTaskPoster(com.scott.annotionprocessor.ThreadMode." + methodParam.threadMode + ");\n");
                        sb.append("ITaskEventDispatcher dispatcher = new ITaskEventDispatcher() {\n public void dispatchTasks(TaskType taskType, ProcessType type,final List taskList) {\n" +
                                "((" + clazzName + ")mScriber)." + methodName + "(" + (methodParam.hasExtas ? "tasks" : "") + ");\n}};");
                        sb.append("\ntaskPoster.enqueue(dispatcher,TaskType." + type + ",processType,tasks);");
                        //sb.append("((" + clazzName + ")mScriber)." + methodName + "(" + (methodParam.hasExtas ? "tasks" : "") + ");\n");
                    }
                }
            }
            sb.append("\nbreak;\n");
        }
        sb.append("}");
        return sb.toString();
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        types.add(TaskSubscriber.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
