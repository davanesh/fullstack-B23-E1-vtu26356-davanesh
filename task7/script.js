document.addEventListener('DOMContentLoaded', () => {
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const feedbackInput = document.getElementById('feedback');
    const submitBtn = document.getElementById('submitBtn');
    const confirmationMsg = document.getElementById('confirmationMsg');
    const form = document.getElementById('feedbackForm');

    // Validation definitions
    const validators = {
        name: {
            element: nameInput,
            errorId: 'nameError',
            validate: (val) => val.trim().length >= 3
        },
        email: {
            element: emailInput,
            errorId: 'emailError',
            validate: (val) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val)
        },
        feedback: {
            element: feedbackInput,
            errorId: 'feedbackError',
            validate: (val) => val.trim().length >= 10
        }
    };

    // Reusable validation logic
    function validateField(fieldKey) {
        const field = validators[fieldKey];
        const isValid = field.validate(field.element.value);
        const errorMsg = document.getElementById(field.errorId);
        
        if (!isValid && field.element.value.length > 0) {
            errorMsg.style.display = 'block';
            field.element.style.borderColor = 'red';
            return false;
        } else {
            errorMsg.style.display = 'none';
            // Reset to default or focus color handled by CSS
            if(isValid) field.element.style.borderColor = 'green';
            else field.element.style.borderColor = '#ccc';
            return isValid;
        }
    }

    // Keypress/Input validation
    nameInput.addEventListener('input', () => validateField('name'));
    emailInput.addEventListener('input', () => validateField('email'));
    feedbackInput.addEventListener('input', () => validateField('feedback'));

    // Highlight labels on input hover
    const inputs = [nameInput, emailInput, feedbackInput];
    inputs.forEach(input => {
        input.addEventListener('mouseenter', () => {
            input.previousElementSibling.style.color = '#007bff';
        });
        input.addEventListener('mouseleave', () => {
            input.previousElementSibling.style.color = '#555';
        });
    });

    // Double click to submit
    submitBtn.addEventListener('dblclick', () => {
        const isNameValid = validateField('name');
        const isEmailValid = validateField('email');
        const isFeedbackValid = validateField('feedback');

        if (isNameValid && isEmailValid && isFeedbackValid) {
            form.style.display = 'none';
            confirmationMsg.classList.remove('hidden');
        } else {
            alert('Please fix the errors before submitting.');
        }
    });

    // Prevent single click from doing anything to demonstrate dblclick requirement
    submitBtn.addEventListener('click', (e) => {
        e.preventDefault();
        console.log("Single click ignored. Double-click required.");
    });
});
