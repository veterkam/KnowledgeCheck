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