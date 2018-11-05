$( document ).ready(
    function() {
        // Remove answer button click
        $(document).on(
            'click',
            '[data-role="remove-answer"]',
            function(e) {
                e.preventDefault();
                var questionContainer = $(this).closest('[data-role="question-container"]');
                var countVisibleAnswers = questionContainer.find('[data-role="answer-container"]:visible').length;
                //console.log("countVisibleAnswers " + countVisibleAnswers);
                if(countVisibleAnswers > 1) {
                    var answerContainer = $(this).closest('[data-role="answer-container"]');
                    var id = answerContainer.find('[data-role="answer-id"]:first').val();//attr("value");
                    //console.log(id);

                    if (id != undefined && id != "" && id > -1) {
                        answerContainer.find('[data-role="answer-removing-flag"]:first').val(1);//attr("value", 1);
                        answerContainer.hide();
                    } else {
                        answerContainer.remove();
                    }
                }
            }
        );

        // Add answer button click
        $(document).on(
            'click',
            '[data-role="add-answer"]',
            function(e) {
                e.preventDefault();
                var curAnswerContainer = $(this).closest('[data-role="answer-container"]');
                var newAnswerContainer = curAnswerContainer.clone();
                newAnswerContainer.find('input').val('');
                //newAnswerContainer.find('input').attr('value', '');
                newAnswerContainer.find(':checkbox').prop( "checked", false );
                curAnswerContainer.after(newAnswerContainer);
            }
        );

        // Remove question button click
        $(document).on(
            'click',
            '[data-role="remove-question"]',
            function(e) {
                e.preventDefault();
                var testContainer = $(this).closest('[data-role="test-container"]');
                var countVisibleQuestions = testContainer.find('[data-role="question-container"]:visible').length;

                //console.log("count visible elements " + countVisibleQuestions);
                if(countVisibleQuestions > 1) {
                    // Find id of the question, if question has it
                    var questionContainer = $(this).closest('[data-role="question-container"]');
                    var id = questionContainer.find('[data-role="question-id"]:first').val();//attr("value");
                    //console.log(id);

                    if (id != undefined && id != "" && id > -1) {
                        // If question has id, then only hide it
                        // We will use id for removing record from DB
                        questionContainer.find('[data-role="question-removing-flag"]:first').val(1);//attr("value", 1);
                        questionContainer.hide();
                    } else {
                        questionContainer.remove();
                    }
                }
            }
        );

        // Add question button click
        $(document).on(
            'click',
            '[data-role="add-question"]',
            function(e) {
                e.preventDefault();
                var curQuestionContainer = $(this).closest('[data-role="question-container"]');
                var newQuestionContainer = curQuestionContainer.clone();
                //newQuestionContainer.find('input, textarea').attr('value', '');
                newQuestionContainer.find('input, textarea').val('');
                newQuestionContainer.find(':checkbox').prop( "checked", false );
                curQuestionContainer.after(newQuestionContainer);
                newQuestionContainer.find('[data-role="answer-container"]:hidden').remove();
            }
        );

        $(document).on(
            'click',
            '[data-role="indexing"]',
            function(e) {
                indexing();
            }
        );
    }
);
