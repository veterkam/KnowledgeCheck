$(
    function() {
        // Remove answer button click
        $(document).on(
            'click',
            '[data-role="remove answer"]',
            function(e) {
                e.preventDefault();
                var container = $(this).closest('.form-group');
                if(container.children().filter('.input-group').length > 1) {
                    $(this).closest('.input-group').remove();
                }

            }
        );

        // Add answer button click
        $(document).on(
            'click',
            '[data-role="add answer"]',
            function(e) {
                e.preventDefault();
                var container = $(this).closest('.form-group');
                var newElementGroup = container.children().filter('.input-group:first').clone();

                newElementGroup.find('input').each(function(){
                    $(this).val('');
                });
                $(this).closest('.input-group').after(newElementGroup);
            }
        );

        // Remove question button click
        $(document).on(
            'click',
            '[data-role="remove question"]',
            function(e) {
                e.preventDefault();
                var container = $(this).closest('form');
                if(container.children().filter('.card').length > 1) {
                    $(this).closest('.card').remove();
                }
            }
        );

        // Add question button click
        $(document).on(
            'click',
            '[data-role="add question"]',
            function(e) {
                e.preventDefault();
                var newElementGroup = $(this).closest('.card').clone();
                newElementGroup.find('input, textarea').each(function(){
                    $(this).val('');
                });
                $(this).closest('.card').after(newElementGroup);
            }
        );



        function indexing() {
            // modify answers[] to answers[m], correct[][] to correct[m][k]
            var container = $('.container:first').children().filter('form:first');
            var index = 0;
            container.children().filter('.card').each(function(){
                var answerIndex = 0;
                // first find checkboxes, because when checkbox unchecked there is no value in post data
                $(this).find('.input-group').each(function() {
                    $(this).find('input, textarea').each(function () {
                        var name = $(this).attr('name');
                        // correct[][] to correct[m][k]
                        name = name.replace(/\[\]\[\]/, '[' + index + '][' + answerIndex + ']');
                        $(this).attr('name', name);
                    });
                    answerIndex++
                });

                // last find answers
                $(this).find('input, textarea').each(function(){
                    var name = $(this).attr('name');
                    // answers[] to answers[m]
                    name = name.replace(/\[\]/, '['+ index + ']');
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
    }
);