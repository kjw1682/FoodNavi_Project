// 운동 시간 입력 필드를 찾습니다.
var exerciseTimeInput = document.getElementById('exerciseTime');

// -30분 버튼에 이벤트 리스너를 추가합니다.
document.getElementById('exerciseTimeM30').addEventListener('click', function() {
    // 입력 필드의 값이 30 이상인 경우에만 30을 뺍니다.
    if (exerciseTimeInput.value >= 30) {
        exerciseTimeInput.value -= 30;
    }
});

// +30분 버튼에 이벤트 리스너를 추가합니다.
document.getElementById('exerciseTimeP30').addEventListener('click', function() {
    // 입력 필드의 값을 30 증가시킵니다.
    exerciseTimeInput.value = Number(exerciseTimeInput.value) + 30;
});

$(function() {
    // "custom.combobox"라는 이름의 새로운 위젯을 생성합니다.
    $.widget("custom.combobox", {
        // 위젯이 생성될 때 호출되는 함수입니다.
        _create: function() {
            // 이 요소 바로 뒤에 <span> 요소를 추가하고, "custom-combobox" 클래스를 추가합니다.
            this.wrapper = $("<span>")
                .addClass("custom-combobox")
                .insertAfter(this.element);
            // 원래의 select 요소를 숨깁니다.
            this.element.hide();
            // 자동완성 기능과 모든 항목 보기 버튼을 생성합니다.
            this._createAutocomplete();
            this._createShowAllButton();
        },
        // 자동완성 기능을 생성하는 함수입니다.
        _createAutocomplete: function() {
            // 현재 선택된 옵션을 찾고, 그 값이 있으면 그 텍스트를, 없으면 빈 문자열을 사용합니다.
            var selected = this.element.children(":selected"),
                value = selected.val() ? selected.text() : "";
            // input 요소를 생성하고, 위에서 찾은 값을 설정합니다.
            this.input = $("<input>")
                .appendTo(this.wrapper)
                .val(value)
                .attr("title", "")
                .addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left")
                .autocomplete({
                    delay: 0,
                    minLength: 0,
                    source: $.proxy(this, "_source")
                })
                .tooltip({
                    classes: {
                        "ui-tooltip": "ui-state-highlight"
                    }
                });
            // input 요소에 이벤트 리스너를 추가합니다.
            this._on(this.input, {
                // 사용자가 자동완성 목록에서 항목을 선택했을 때 호출되는 함수입니다.
                autocompleteselect: function(event, ui) {
                    ui.item.option.selected = true;
                    this._trigger("select", event, {
                        item: ui.item.option
                    });
                },
                // 사용자가 input 요소의 값을 변경했을 때 호출되는 함수입니다.
                autocompletechange: "_removeIfInvalid"
            });
        },
        // 모든 항목 보기 버튼을 생성하는 함수입니다.
        _createShowAllButton: function() {
            var input = this.input,
                wasOpen = false;
            // 버튼 요소를 생성하고, 이 요소를 클릭하면 자동완성 목록이 표시되도록 합니다.
            $("<a>")
                .attr("tabIndex", -1)
                .attr("title", "Show All Items")
                .tooltip()
                .appendTo(this.wrapper)
                .button({
                    icons: {
                        primary: "ui-icon-triangle-1-s"
                    },
                    text: false
                })
                .removeClass("ui-corner-all")
                .addClass("custom-combobox-toggle ui-corner-right")
                .on("mousedown", function() {
                    wasOpen = input.autocomplete("widget").is(":visible");
                })
                .on("click", function() {
                    input.trigger("focus");
                    if (wasOpen) {
                        return;
                    }
                    input.autocomplete("search", "");
                });
        },
        // 자동완성 목록의 항목을 생성하는 함수입니다.
        _source: function(request, response) {
            var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
            response(this.element.children("option").map(function() {
                var text = $(this).text();
                if (this.value && (!request.term || matcher.test(text)))
                    return {
                        label: text,
                        value: text,
                        option: this
                    };
            }));
        },
        // 사용자가 입력한 값이 유효하지 않은 경우, input 요소의 값을 초기화하는 함수입니다.
        _removeIfInvalid: function(event, ui) {
            if (ui.item) {
                return;
            }
            var value = this.input.val(),
                valueLowerCase = value.toLowerCase(),
                valid = false;
            this.element.children("option").each(function() {
                if ($(this).text().toLowerCase() === valueLowerCase) {
                    this.selected = valid = true;
                    return false;
                }
            });
            if (valid) {
                return;
            }
            this.input
                .val("")
                .attr("title", value + " didn't match any item")
                .tooltip("open");
            this.element.val("");
            this._delay(function() {
                this.input.tooltip("close").attr("title", "");
            }, 2500);
            this.input.autocomplete("instance").term = "";
        },
        // 위젯이 제거될 때 호출되는 함수입니다.
        _destroy: function() {
            this.wrapper.remove();
            this.element.show();
        }
    });
    // 위에서 생성한 위젯을 적용합니다.
    $("#combobox").combobox();

});