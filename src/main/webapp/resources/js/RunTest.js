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


        // Init timer for updating test remaining time
        var remainingTime =  +$('[data-role="test-remaining-time"]:first').html();

        var timerId;
        if(remainingTime > 0) {
            writeRemainingTime();
            $('[data-role="test-remaining-time"]:first').attr("hidden", false);
            timerId = setInterval(updateRemainingTime, 1000);
        }
        
        function updateRemainingTime() {
            if(remainingTime == 0) {
                clearInterval(timerId);
                $('[data-role="indexing"]:first').click();
                return;
            }

            remainingTime--;
            writeRemainingTime()
        }

        function writeRemainingTime() {
            $('[data-role="test-remaining-time"]:first').html( convertToTimePeriod(remainingTime) );
        }

        function convertToTimePeriod(seconds) {

            var hh = Math.floor(seconds / 3600 );
            seconds -= hh * 3600;
            var mm = Math.floor(seconds / 60);
            seconds -= mm * 60;
            var ss = seconds;

            var HH = (hh > 9) ? "" + hh : "0" + hh;
            var MM = (mm > 9) ? "" + mm : "0" + mm;
            var SS = (ss > 9) ? "" + ss : "0" + ss;

            return HH + ":" + MM + ":" + SS;
        }



    }
);
