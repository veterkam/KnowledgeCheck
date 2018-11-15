$( document ).ready(
    function() {
        // Remove subject button click
        $(document).on(
            'click',
            '[data-role="remove-subject"]',
            function(e) {
                e.preventDefault();
                var subjectContainer = $(this).closest('[data-role="subject-container"]');
                var id = subjectContainer.find('[data-role="subject-id"]:first').val();

                if (id != undefined && id != "" && id > -1) {
                    subjectContainer.find('[data-role="subject-remove-flag"]:first').val(1);
                    subjectContainer.hide();
                } else {
                    subjectContainer.remove();
                }

                subjectListIndexing();
            }
        );

        // Add subject button click
        $(document).on(
            'click',
            '[data-role="add-subject"]',
            function(e) {
                e.preventDefault();
                var lastSubjectContainer = $('[data-role="subject-container"]:last');
                var newAnswerContainer = lastSubjectContainer.clone();
                newAnswerContainer.find('input').val('');
                lastSubjectContainer.after(newAnswerContainer);
                newAnswerContainer.show();
                subjectListIndexing();
                $('[data-role="subject-input"]:last').focus();
                $('[data-role="subject-id"]:last').val(-1);

            }
        );

        $(document).on(
            'change',
            '[data-role="subject-input"]',
            function(e) {
                var subjectContainer = $(this).closest('[data-role="subject-container"]');
                subjectContainer.find('[data-role="subject-modify-flag"]:first').val(1);
            }
        );

        function subjectListIndexing() {
            var index = 1;
            $('[data-role="num-container"]:visible').each(function(){
                $(this).text(index++);
            });
        }

    }
);
