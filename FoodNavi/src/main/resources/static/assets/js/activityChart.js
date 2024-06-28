Chart.defaults.color = '#eee'; // 전역으로 글씨 색상을 흰색으로 변경

var ctx = document.getElementById('myChart').getContext('2d');

fetch('/activities/data/last-week')
    .then(response => response.json())
    .then(data => {
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['일', '월', '화', '수', '목', '금', '토'],
                datasets: [{
                    label: '운동시간',
                    data: data, // 운동 시간을 데이터로 사용
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)',
                    borderWidth: 1
                }]
            },
        });
    });

