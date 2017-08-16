jQuery(document).ready(function ($) {
    var $form_modal = $('.cd-user-modal'),
        $form_login = $form_modal.find('#cd-login'),
        $form_forgot_password = $form_modal.find('#cd-reset-password'),
        $form_modal_tab = $('.cd-switcher'),
        $back_to_login_link = $form_forgot_password.find('.cd-form-bottom-message a'),
        $submit_input = $("input[type=submit]"),
        $user_msg = $("#user-msg"),
        $pass_msg = $("#pass-msg"),
        $signin_in = $('.cd-signin');

    //未登录显示登录窗口
    var uname = $form_modal.attr("uname");
    if ("" == uname || "undefined" == uname) {
        $form_modal.addClass("is-visible");
        $form_login.addClass("is-selected");
    } else {
        $form_modal.removeClass("is-visible");
        $form_login.removeClass("is-selected");
    }

    //open modal
    $signin_in.on('click', function (event) {
        //show modal layer
        $form_modal.addClass('is-visible');
        //show the selected form
        login_selected();
    });

    //close modal
    $('.cd-user-modal').on('click', function (event) {
        if ($(event.target).is($form_modal) || $(event.target).is('.cd-close-form')) {
            $form_modal.removeClass('is-visible');
        }
    });

    //close modal when clicking the esc keyboard button
    $(document).keyup(function (event) {
        if (event.which == '27') {
            $form_modal.removeClass('is-visible');
        }
    });

    //switch from a tab to another
    $form_modal_tab.on('click', function (event) {
        event.preventDefault();
        login_selected();
    });


    //back to login from the forgot-password form
    $back_to_login_link.on('click', function (event) {
        event.preventDefault();
        login_selected();
    });

    function login_selected() {
        $form_login.addClass('is-selected');
        $form_forgot_password.removeClass('is-selected');
    }


    //REMOVE THIS - it's just to show error messages
    $form_login.find('input[type="submit"]').on('click', function (event) {
        event.preventDefault();
        $form_login.find('input[type="email"]').toggleClass('has-error').next('span').toggleClass('is-visible');
    });


    //IE9 placeholder fallback
    //credits http://www.hagenburger.net/BLOG/HTML5-Input-Placeholder-Fix-With-jQuery.html
    if (!Modernizr.input.placeholder) {
        $('[placeholder]').focus(function () {
            var input = $(this);
            if (input.val() == input.attr('placeholder')) {
                input.val('');
            }
        }).blur(function () {
            var input = $(this);
            if (input.val() == '' || input.val() == input.attr('placeholder')) {
                input.val(input.attr('placeholder'));
            }
        }).blur();
        $('[placeholder]').parents('form').submit(function () {
            $(this).find('[placeholder]').each(function () {
                var input = $(this);
                if (input.val() == input.attr('placeholder')) {
                    input.val('');
                }
            })
        });
    }

    $submit_input.click(function () {
        var uname = $("#signin-uname").val(),
            password = $("#signin-password").val();

        if (uname == '' || uname == 'undefined') {
            alert('请输入用户名');
            return false;
        }

        if (password == '' || password == 'undefined') {
            alert('请输入密码');
            return false;
        }

        $.ajax({
            type: 'POST',
            url: contextPath + '/login/',
            data: {
                uname: uname,
                password: password
            },
            success: function (result) {
                console.log(result);
                if (result.status.code != 1001) {
                    $pass_msg.html(result.status.msg);
                    $form_login.find('#signin-password').toggleClass('has-error').next('span').toggleClass('is-visible');
                } else {
                    window.location.reload();
                }
            },
            dataType: 'json'
        });
    });

});


//credits http://css-tricks.com/snippets/jquery/move-cursor-to-end-of-textarea-or-input/
jQuery.fn.putCursorAtEnd = function () {
    return this.each(function () {
        // If this function exists...
        if (this.setSelectionRange) {
            // ... then use it (Doesn't work in IE)
            // Double the length because Opera is inconsistent about whether a carriage return is one character or two. Sigh.
            var len = $(this).val().length * 2;
            this.setSelectionRange(len, len);
        } else {
            // ... otherwise replace the contents with itself
            // (Doesn't work in Google Chrome)
            $(this).val($(this).val());
        }
    });
};
