package com.starfireaviation.questions;

import com.starfireaviation.questions.service.ACSService;
import com.starfireaviation.questions.service.AnswerService;
import com.starfireaviation.questions.service.BinaryDataService;
import com.starfireaviation.questions.service.ChapterService;
import com.starfireaviation.questions.service.FigureSectionService;
import com.starfireaviation.questions.service.GroupService;
import com.starfireaviation.questions.service.ImageService;
import com.starfireaviation.questions.service.LibraryService;
import com.starfireaviation.questions.service.QuestionACSService;
import com.starfireaviation.questions.service.QuestionRefImageService;
import com.starfireaviation.questions.service.QuestionReferenceService;
import com.starfireaviation.questions.service.QuestionService;
import com.starfireaviation.questions.service.QuestionTestService;
import com.starfireaviation.questions.service.RefService;
import com.starfireaviation.questions.service.SourceService;
import com.starfireaviation.questions.service.SubjectMatterCodeService;
import com.starfireaviation.questions.service.TestService;
import com.starfireaviation.questions.service.TextConstService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Update {

    public static void main(final String[] args) {
        String secretKey = "";
        String initVector = "";
        String mysqlHost = "";
        String mysqlUser = "";
        String mysqlPass = "";
        String coursesToUpdate = "";
        if (args.length == 6) {
            secretKey = args[0];
            initVector = args[1];
            mysqlHost = args[2];
            mysqlUser = args[3];
            mysqlPass = args[4];
            coursesToUpdate = args[5];
            new Update(secretKey, initVector, mysqlHost, mysqlUser, mysqlPass, coursesToUpdate);
        }
    }

    public Update(final String secretKey,
                  final String initVector,
                  final String mysqlHost,
                  final String mysqlUser,
                  final String mysqlPass,
                  final String coursesToUpdate) {
        List<String> courses = new ArrayList<>(List.of(coursesToUpdate.split(",")));
        courses
                .stream()
                .distinct()
                .forEach(course -> {
                    final QuestionService questionService = new QuestionService(course, mysqlHost, mysqlUser, mysqlPass);
                    questionService.initCipher(secretKey, initVector);
                    questionService.run();
                    final AnswerService answerService = new AnswerService(course, mysqlHost, mysqlUser, mysqlPass);
                    answerService.initCipher(secretKey, initVector);
                    answerService.run();
                    ImageService imageService = new ImageService(course, mysqlHost, mysqlUser, mysqlPass);
                    imageService.run();
                    ACSService acsService = new ACSService(course, mysqlHost, mysqlUser, mysqlPass);
                    acsService.run();
                    BinaryDataService binaryDataService = new BinaryDataService(course, mysqlHost, mysqlUser, mysqlPass);
                    binaryDataService.run();
                    ChapterService chapterService = new ChapterService(course, mysqlHost, mysqlUser, mysqlPass);
                    chapterService.run();
                    FigureSectionService figureSectionService = new FigureSectionService(course, mysqlHost, mysqlUser, mysqlPass);
                    figureSectionService.run();
                    GroupService groupService = new GroupService(course, mysqlHost, mysqlUser, mysqlPass);
                    groupService.run();
                    LibraryService libraryService = new LibraryService(course, mysqlHost, mysqlUser, mysqlPass);
                    libraryService.run();
                    QuestionACSService questionACSService = new QuestionACSService(course, mysqlHost, mysqlUser, mysqlPass);
                    questionACSService.run();
                    QuestionRefImageService questionRefImageService = new QuestionRefImageService(course, mysqlHost, mysqlUser, mysqlPass);
                    questionRefImageService.run();
                    QuestionReferenceService questionReferenceService = new QuestionReferenceService(course, mysqlHost, mysqlUser, mysqlPass);
                    questionReferenceService.run();
                    QuestionTestService questionTestService = new QuestionTestService(course, mysqlHost, mysqlUser, mysqlPass);
                    questionTestService.run();
                    RefService refService = new RefService(course, mysqlHost, mysqlUser, mysqlPass);
                    refService.run();
                    SubjectMatterCodeService subjectMatterCodeService = new SubjectMatterCodeService(course, mysqlHost, mysqlUser, mysqlPass);
                    subjectMatterCodeService.run();
                    SourceService sourceService = new SourceService(course, mysqlHost, mysqlUser, mysqlPass);
                    sourceService.run();
                    TestService testService = new TestService(course, mysqlHost, mysqlUser, mysqlPass);
                    testService.run();
                    TextConstService textConstService = new TextConstService(course, mysqlHost, mysqlUser, mysqlPass);
                    textConstService.run();
                });
        final WebClient webClient = WebClient.create("https://questions.starfireaviation.com");
        webClient
                .method(HttpMethod.POST)
                .uri("/api/answers/updatechoices")
                .retrieve();

    }

}
