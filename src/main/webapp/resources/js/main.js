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



        function indexing() {
            // modify answers[] to answers[m], correct[][] to correct[m][k]
            var index = 0;

            $('[data-role="question-container"]').each(function(){
                var answerIndex = 0;
                // data from fields with the same names is sent to the server as an array
                // (for example corrects[]), but data from checkboxes is displayed in the
                // array only if it is checked, so we use the two-dimensional array corrects[][]
                // question[1] => corrects[1][]
                // first: find fields with [][] (corrects)
                $(this).find('[data-role="answer-container"]').each(function() {
                    $(this).find('input').each(function () {
                        var name = $(this).attr('name');
                        // correct[][] to correct[m][k]
                        name = name.replace(/\[\d*\]\[\d*\]$/, '[' + index + '][' + answerIndex + ']');
                        $(this).attr('name', name);
                    });
                    answerIndex++
                });

                // second: find fields with [] (answers descriptions, ids, remove flags)
                $(this).find('input, textarea').each(function(){
                    var name = $(this).attr('name');
                    // answers[] to answers[m]
                    name = name.replace(/([^\]]+)(\[\d*\])$/, '$1['+ index + ']');
                    $(this).attr('name', name);
                });

                index++;
            });
        }

        $(document).on(
            'click',
            '[data-role="indexing"]',
            function(e) {
                indexing();
            }
        );

        $(document).on(
            'click',
            '[data-role="question-carousel-prev"]',
            function(e) {
                e.preventDefault();
                $("#question-carousel").carousel("prev");
            }
        );

        $(document).on(
            'click',
            '[data-role="question-carousel-next"]',
            function(e) {
                e.preventDefault();
                $("#question-carousel").carousel("next");
            }
        );

        $(document).on(
            'slide.bs.carousel',
            '#question-carousel',
            function(e){
                progressTestBar(e.to);
            });

        function progressTestBar(index){
                var questionCount = $('[data-role="question-container"]').length;
                var percent = Math.trunc(100 * (index + 1) / questionCount);
                $("#test-progress-bar").css('width', percent + '%');
                $("#test-progress-bar").html( (index + 1)  + "/" + questionCount);
            };

        progressTestBar(0);
    }
);
