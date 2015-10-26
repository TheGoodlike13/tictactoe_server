package eu.goodlike;

@RestController
public class TicTacToeController {

    @RequestMapping(method = POST, produces = APPLICATION_JSON_UTF8, consumes = APPLICATION_JSON_UTF8)
    @ResponseBody
    public FetchedLesson create(PartitionAccount account, @RequestBody SuppliedLesson lesson) {
        Null.check(lesson).ifAny(() -> new InvalidRequestException("Lesson cannot be null"));
        LOG.info("User {} is creating a lesson: {}", account, lesson);

        SuppliedLessonWithTimestamp suppliedLesson = lessonTransformer.from(lesson, true);
        FetchedLesson createdLesson = lessonCommands.create(suppliedLesson, account.partitionId());
        LOG.info("Successfully created lesson: {}", lesson);

        return createdLesson;
    }

}
