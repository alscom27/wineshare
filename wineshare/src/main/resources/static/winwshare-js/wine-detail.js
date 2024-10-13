
const stars = document.querySelectorAll('.my-rating i');

stars.forEach(star => {
    star.addEventListener('click', () => {
        const rating = star.getAttribute('data-value');

        // 별점 설정
        stars.forEach(s => {
            if (s.getAttribute('data-value') <= rating) {
                s.classList.add('active'); // 활성화된 별
            } else {
                s.classList.remove('active'); // 비활성화된 별
            }
        });

        console.log(`선택한 별점: ${rating}`); // 선택한 별점 출력
    });
});
