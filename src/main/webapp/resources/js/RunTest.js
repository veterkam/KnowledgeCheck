$( document ).ready(
    function() {
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

        $(document).on(
            'click',
            '[data-role="indexing"]',
            function(e) {
                indexing();
            }
        );

        progressTestBar(0);
    }
);
