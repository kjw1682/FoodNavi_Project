$(document).ready(function() {
	$.ajax({
	    type: 'GET',
	    url: '/load_userVo',
	    dataType: 'json',
	    success: function (data) {
	        var carbohydrateChart = document.getElementById('carbohydrateChart').querySelector('.chart-bar');
			var proteinChart = document.getElementById('proteinChart').querySelector('.chart-bar');
			var fatChart = document.getElementById('fatChart').querySelector('.chart-bar');
			var kcalChart = document.getElementById('kcalChart').querySelector('.chart-bar');
			
			// 각 차트의 값을 0에서 100 사이의 값으로 설정합니다.
			var carbohydrateValue = data.carbToday > data.properCarb ? 100 : data.carbToday*100/data.properCarb;
			var proteinValue = data.prtToday > data.properPrt ? 100 : data.prtToday*100/data.properPrt;
			var fatValue = data.fatToday > data.properFat ? 100 : data.fatToday*100/data.properFat;
			var kcalValue = data.kcalToday > data.EER ? 100 : data.kcalToday*100/data.EER;
			
			// 각 차트의 현재 값을 나타내는 바의 너비를 변경하여 차트의 값을 표시합니다.
			carbohydrateChart.style.width = carbohydrateValue + '%';
			proteinChart.style.width = proteinValue + '%';
			fatChart.style.width = fatValue + '%';
			kcalChart.style.width = kcalValue + '%';
			
			// 차트의 애니메이션을 시작합니다.
		    animateChart("carbohydrateChart", data.carbToday > data.properCarb ? data.properCarb : data.carbToday, data.properCarb);
		    animateChart("proteinChart", data.prtToday > data.properPrt ? data.properPrt : data.prtToday, data.properPrt);
		    animateChart("fatChart", data.fatToday > data.properFat ? data.properFat : data.fatToday, data.properFat);
		    animateChart("kcalChart", data.kcalToday > data.EER ? data.EER : data.kcalToday, data.EER);
	    },
	    error: function () {
			alert("차트오류");
	    }
	});	
});


function animateChart(chartId, currentValue, maxValue) {
    var chart = document.getElementById(chartId);
    var chartBar = chart.querySelector(".chart-bar");
    var percentage = (currentValue / maxValue) * 100;

    var currentWidth = 0;
    var interval = setInterval(function() {
        if (currentWidth >= percentage) {
            clearInterval(interval);
        } else {
            currentWidth++;
            chartBar.style.width = currentWidth + "%";
        }
    }, 20); // 20ms 간격으로 너비를 증가시킵니다.
}