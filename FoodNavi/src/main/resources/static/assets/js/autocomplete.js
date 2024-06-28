function initAutocomplete() {
    $("input[name='ingredient']").autocomplete({
        source: function(request, response) {
            // AJAX 요청을 사용하여 서버에서 자동완성 데이터를 가져옵니다.
            $.ajax({
                url: "/ingredients/search",
                type: "GET",
                data: {
                    term: request.term
                },
                success: function(data) {
                    // 가져온 데이터를 자동완성 목록에 표시합니다.
                    response(data);
                }
            });
        },
        minLength: 1,

        select: function(event, ui) {
            // 사용자가 자동완성 목록에서 항목을 선택하면 입력 필드의 값을 해당 항목의 값으로 설정합니다.
            $(this).val(ui.item.value);
        },
        change: function(event, ui) {
            // 입력 필드의 새 값이 자동완성 목록의 항목과 일치하지 않으면
            if (!ui.item) {
                // 이벤트를 취소
                event.preventDefault();
                // 입력 필드를 지우고
                $(this).val("");
                // 경고 메시지를 표시합니다.
                alert("사용할 수 없는 재료입니다.");
            }
        },
        focus: function(event, ui) {
            // 사용자가 키보드의 방향키를 사용하여 목록 항목에 포커스를 맞추었을 때 입력 필드의 값을 변경하지 않도록 합니다.
            event.preventDefault();
        }
    });
}

// 값이 비어 있는 경우
document.querySelector('form').addEventListener('submit', function(event) {
    var inputs = document.querySelectorAll('input[type="number"], input[type="text"]');
    for(var i = 0; i < inputs.length; i++) {
        if(inputs[i].value === '') {
            // 이벤트를 취소
            event.preventDefault();
            alert('모든 값을 채워주세요');
            $('input[name="ingredient"]').focus();
            return;
        }
    }
});