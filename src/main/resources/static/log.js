function login() {
    $(".email-login").show();
    $(".email-signup").hide();
    $("#login-box-link").addClass("active");
    $("#signup-box-link").removeClass("active");
}

function reg() {
    $(".email-login").hide();
    $(".email-signup").show();
    $("#login-box-link").removeClass("active");
    $("#signup-box-link").addClass("active");
}

function regsend() {
    var email = $("#signupEmail");
    var password = $("#signupPass1");
    var emailError = $("#regError");
    var passError = $("#passError");

    if (email.val() == "") {
        emailError.text('Empty field');
    } else emailError.text('');
    if (password.val() != $("#signupPass2").val()) {
        passError.text('Passwords do not match');
    } else passError.text('');
    if (email.val() != "" && password.val() == $("#signupPass2").val())
        $.ajax({
            type: 'POST',
            url: '/newuser',
            data: {
                login: email.val(),
                password: password.val()
            },
            success: function (msg) {
                window.location = "/";
            }
        }).fail(function (msg) {
            emailError.text('User already exists');
        });
}

$(function () {
    var url = new URL(window.location.href);
    var c = url.searchParams.get("error");
    if (c!=null){
        $("#logError").text('Invalid login or password');
    }

    $("#login-box-link").click(function () {
        login();
    });
    $("#signup-box-link").click(function () {
        reg();
    });
    $("#login").click(function () {
        loginsend();
    });
    $("#signup").click(function () {
        regsend();
    });
});