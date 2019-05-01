var fileNum;
var repeater;
var uploadButton;

function connect() {
    var data = new FormData($('#form'));
    var files = document.getElementById('files').files;
    var i;
    for (i = 0; i < files.length; i++)
        data.append('files[]', files[i]);

    uploadButton.addClass("inactive");
    uploadButton.removeClass("upload");
    uploadButton.attr("disabled", true);

    $.ajax({
        url: '/files/upload',
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        method: 'POST',
        type: 'POST', // For jQuery < 1.9
        success: function () {
            repeater = setInterval(check, 500);
        }
    });
}

function check() {
    if (!document.hidden) {
        $.getJSON('/files/updating', function (data) {
            console.log(data.count);
            if (data.count > fileNum) {
                loadData(0);
                loadPages()
            }
        }).fail(function () {
            if (uploadButton.hasClass("inactive")) {
                uploadButton.addClass("upload");
                uploadButton.removeClass("inactive");
                uploadButton.attr("disabled", false);
                clearInterval(repeater);
            }
        });
    }
}

function loadPages() {
    $.getJSON('/files/count', function (data) {
        fileNum = data.count;
        var pageCount = (data.count / data.pageSize) +
            (data.count % data.pageSize > 0 ? 1 : 0);
        var i;
        $("#pages").empty();
        for (i = 1; i <= pageCount; i++) {
            $('#pages').append(
                $('<button>').attr('class', 'page-link').attr('id', i - 1)
                    .append('Page ' + i));
        }
    });

    $("#pages").on("click", ".page-link", function (event) {
        loadData(event.target.id);
    });
}

function loadData(page) {
    $.getJSON('/files?page=' + page, function (data) {
        $("#conversation > tbody").empty();
        var i;
        for (i = 0; i < data.length; i++) {
            $('#conversation > tbody:last-child').append(
                $('<tr id=' + data[i].name + '>')
                    .append($('<td>').append('<input type="checkbox" class="check" id="' + data[i].name + '">'))
                    .append($('<td>').append(data[i].name))
                    .append($('<td>').append(data[i].date))
                    .append($('<td>').append(formatBytes(data[i].size)))
            );
        }
    });
}

function formatBytes(bytes, decimals) {
    if (bytes === 0) return '0 Bytes';
    var k = 1024,
        dm = decimals <= 0 ? 0 : decimals || 2,
        sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
        i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
}


function deleteFiles() {
    if ($('input:checkbox').is(':checked')) {
        var checkboxes = {'names[]': []};
        $(".check:checkbox:checked").each(function () {
            checkboxes['names[]'].push($(this).attr('id'));
        });
        console.log(checkboxes);
        $.post("/files/delete", checkboxes, function (data, status) {
            $(".check:checkbox:checked").each(function () {
                document.getElementById($(this).attr('id')).remove();
            });
        });
    }
}

function downloadFiles() {
    if ($('input:checkbox').is(':checked')) {
        var url = "/files/download/";
        $(".check:checkbox:checked").each(function () {
            url = url.concat(':').concat($(this).attr('id'));
        });
        var link = document.createElement('a');
        link.href = url;
        link.download = "Archive.zip";
        link.click();
    }
}

$(function () {
    loadPages();
    loadData(0);
    $("#delete").click(function () {
        deleteFiles();
    });
    $("#download").click(function () {
        downloadFiles();
    });
    $('#checkAll').click(function () {
        var checkedStatus = this.checked;
        $('.check').each(function () {
            $(this).prop('checked', checkedStatus);
        });
    });
    uploadButton = $("#files");
    uploadButton.on('change', function () {
        connect()
    });
});
