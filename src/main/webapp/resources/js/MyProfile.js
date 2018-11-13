$( document ).ready(
    function() {
        // Remove answer button click
        $(document).on(
            'keydown, keyup',
            '#inputPassword',
            function (e) {
                $('#inputRepeatPassword').prop('disabled', $('#inputPassword').val().length == 0);
            }
        );
    }
);