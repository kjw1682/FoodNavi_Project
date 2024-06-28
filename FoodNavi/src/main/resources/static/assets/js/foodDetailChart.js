Chart.defaults.color = '#eee'; // 전역으로 글씨 색상을 흰색으로 변경

var ctx = document.getElementById('foodChart').getContext('2d');

fetch('/foodDetailChart/data/drawChart')
    .then(response => response.json())
    .then(data => {
        var foodChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['탄수화물', '단백질', '지방'],
                datasets: [{
                    label: ['함량(g)'],
                    data: data, // 운동 시간을 데이터로 사용
                    backgroundColor: [
                        'rgb(255, 99, 132)',
                        'rgb(54, 162, 235)',
                        'rgb(255, 205, 86)'
                    ],
                    hoverOffset: 3
                }]
            },
            options: {
                responsive: false
            }
        });
    });

