package com.smilebat.learntribe.learntribeinquisitve.converters;

import com.smilebat.learntribe.inquisitve.AssessmentType;
import com.smilebat.learntribe.inquisitve.response.AssessmentResponse;
import com.smilebat.learntribe.learntribeinquisitve.dataaccess.jpa.entity.Assessment;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/**
 * Assessment Converter to map between Entities , Request and Response
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
public final class AssessmentConverter {

  /**
   * Converts the {@link Assessment} to {@link AssessmentResponse}.
   *
   * @param assessment the {@link Assessment}
   * @return the {@link AssessmentResponse}
   */
  public AssessmentResponse toResponse(Assessment assessment) {
    AssessmentResponse response = new AssessmentResponse();
    response.setId(assessment.getId());
    response.setName(assessment.getName());
    response.setProgress(assessment.getProgress());
    response.setNumOfQuestions(assessment.getQuestions());

    AssessmentType assessmentType = assessment.getType();
    if (assessmentType != null) {
      response.setType(assessment.getType().toString());
    }

    return response;
  }

  /**
   * Converts the List of {@link Assessment} to List of {@link AssessmentResponse}.
   *
   * @param assessmentList the List of {@link Assessment}
   * @return the List of {@link AssessmentResponse}
   */
  public List<AssessmentResponse> toResponse(List<Assessment> assessmentList) {
    return assessmentList.stream().map(this::toResponse).collect(Collectors.toList());
  }

  //  public static void main(String[] args) {
  //    String u = "\n\n\n1. What is Node.js?\n\nA. Node.js is a server-side scripting language\nB.
  // Node.js is a client-side scripting language\nC. Node.js is a programming language\nD. Node.js
  // is a web development framework\n\nAnswer: C. Node.js is a programming language.\n\n2. What is
  // the event-driven programming model?\n\nA. The event-driven programming model is a way of
  // structuring code where the program flow is dictated by events.\nB. The event-driven programming
  // model is a way of structuring code where the program flow is dictated by user input.\nC. The
  // event-driven programming model is a way of structuring code where the program flow is dictated
  // by the operating system.\nD. The event-driven programming model is a way of structuring code
  // where the program flow is dictated by the code itself.\n\nAnswer: A. The event-driven
  // programming model is a way of structuring code where the program flow is dictated by
  // events.\n\n3. What is an event loop?\n\nA. An event loop is a way of structuring code so that
  // it can be executed asynchronously.\nB. An event loop is a way of structuring code so that it
  // can be executed in a single thread.\nC. An event loop is a way of structuring code so that it
  // can be executed in parallel.\nD. An event loop is a way of structuring code so that it can be
  // executed in multiple threads.\n\nAnswer: A. An event loop is a way of structuring code so that
  // it can be executed asynchronously.\n\n4. What is a callback?\n\nA. A callback is a function
  // that is called in response to an event.\nB. A callback is a function that is called in response
  // to a user action.\nC. A callback is a function that is called in response to a change in
  // state.\nD. A callback is a function that is called in response to an asynchronous
  // operation.\n\nAnswer: D. A callback is a function that is called in response to an asynchronous
  // operation.\n\n5. What is the Node.js runtime?\n\nA. The Node.js runtime is a programming
  // environment that allows Node.js programs to run.\nB. The Node.js runtime is a programming
  // environment that allows Node.js programs to be executed.\nC. The Node.js runtime is a
  // programming environment that allows Node.js programs to be written.\nD. The Node.js runtime is
  // a programming environment that allows Node.js programs to be developed.\n\nAnswer: B. The
  // Node.js runtime is a programming environment that allows Node.js programs to be executed.";
  //    String z = "\n\n1. What is the output of the following code?\n\npublic class Test {\n
  // public static void main(String[] args) {\n      int x = 0;\n      int y = 0;\n      for (int z
  // = 0; z < 5; z++) {\n         if (( ++x > 2 ) && (++y > 2)) {\n            x++;\n         }\n
  //   }\n      System.out.println(x + \\\" \\\" + y);\\n   }\n}\n\nA. 3 3\nB. 4 3\nC. 4 2\nD. 3
  // 2\n\nAnswer: B\n\n2. What is the output of the following code?\n\npublic class Test {\n
  // public static void main(String[] args) {\n      int a = 1;\n      int b = 2;\n      int c =
  // 3;\n      a = b = c * 1 + 2;\n      System.out.println(a + \\\" \\\" + b + \\\" \\\" + c);\n
  // }\n}\n\nA. 3 2 3\nB. 6 2 1\nC. 6 2 3\nD. 3 6 3\n\nAnswer: C\n\n3. What is the output of the
  // following code?\n\npublic class Test {\n   public static void main(String[] args) {\n
  // String s1 = \"Java\";\n      String s2 = \"Java\";\n      StringBuilder sb1 = new
  // StringBuilder();\n      sb1.append(\"Ja\").append(\"va\");\n      System.out.println(s1 ==
  // s2);\n      System.out.println(s1.equals(s2));\n      System.out.println(sb1.toString() ==
  // s1);\n      System.out.println(sb1.toString().equals(s1));\n   }\n}\n\nA. false false false
  // false\nB. true true false false\nC. false true true true\nD. true true true false\n\nAnswer:
  // D\n\n4. Which of the following options are true about the Java 8 Stream API?\n\nI. Streams
  // represent a sequence of elements\nII. Streams can be created from various data sources,
  // including arrays, collections, and I/O resources\nIII. Streams support aggregate
  // operations\nIV. Streams can be transformed\n\nA. I, II, and III\nB. II, III, and IV\nC. I, III,
  // and IV\nD. I, II, III, and IV\n\nAnswer: D\n\n5. What is the output of the following
  // code?\n\nimport java.util.*;\npublic class Test {\n   public static void main(String[] args)
  // {\n      List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);\n
  // list.stream()\n         .filter(x -> x%2 == 0)\n         .map(x -> x * 2)\n
  // .forEach(System.out::println);\n   }\n}\n\nA. 2 4 6 8 10 12 14 16 18 20\nB. 1 2 3 4 5 6 7 8 9
  // 10\nC. 2 4 8 10\nD. 4 8 16\n\nAnswer: A";
  //    String x = "\n\n1. What is the output of the following code?\n\nprint(type(type(int)))\n\nA.
  // <class 'type'>\nB. <class 'int'>\nC. <class 'object'>\nD. None of the above\n\nAnswer: A.
  // <class 'type'>\n\n2. What is the output of the following code?\n\nprint(isinstance(2.0,
  // int))\n\nA. True\nB. False\nC. None of the above\n\nAnswer: B. False\n\n3. What is the output
  // of the following code?\n\nprint(issubclass(bool, int))\n\nA. True\nB. False\nC. None of the
  // above\n\nAnswer: A. True\n\n4. What is the output of the following code?\n\nprint(5//2)\n\nA.
  // 2\nB. 3\nC. 5\nD. None of the above\n\nAnswer: A. 2\n\n5. What is the output of the following
  // code?\n\nprint(5%2)\n\nA. 0\nB. 1\nC. 2\nD. None of the above\n\nAnswer: B. 1";
  //    String[] y = z.split(System.lineSeparator());
  //    String[] y1 = y[0].split("[+-]?([0-9]*[.])?[0-9]+");
  //
  //    //System.out.println(Arrays.asList(y));
  //
  //    for (int i = 0; i<y1.length; i++) {
  //      System.out.println("Index "+i+" text: "+ y1[i]);
  //    }

  // }
}
