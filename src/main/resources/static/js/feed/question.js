document.addEventListener('DOMContentLoaded', function () {
    const questionForm = document.querySelector('.question-form');
    const questionInput = document.getElementById('questionInput');

    questionForm.addEventListener('submit', function (event) {
        if (questionInput.value.trim() === '') {
            alert('질문을 입력해 주세요.');
            event.preventDefault();
        }
    });
});