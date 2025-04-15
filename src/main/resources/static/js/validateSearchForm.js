document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('searchForm');

    form.addEventListener('submit', function(event) {
        if (!validateAtLeastOneFieldFilled()) {
            event.preventDefault();
        }
        else {
            capitalizeFormFields();
        }
    });

    function validateAtLeastOneFieldFilled() {
        const title = document.getElementById('title').value.trim();
        const year = document.getElementById('year').value.trim();
        const artist = document.getElementById('artist').value.trim();
        const album = document.getElementById('album').value.trim();

        //Ensure at least one field has been filled
        if (title === '' && year === '' && artist === '' && album === '') {
            alert('Please fill at least one field');
            return false;
        } else if (year !== '') {
            //Make sure the year makes sense: is a number and is between 1900 and current.
            const yearNum = parseInt(year, 10);
            const currentYear = new Date().getFullYear();
            if (isNaN(yearNum) || yearNum < 1900 || yearNum > currentYear) {
                alert('Please enter a valid year between 1900 and ' + currentYear);
                return false;
            }
        }
        return true;
    }

    // Method to capitilize the first letter of each word, but lower case the remaining
    function capitalizeWords(word) {
        return word.toLowerCase().split(' ').map(word =>
            word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
    }

    function capitalizeFormFields() {
        const fields = ['title', 'artist', 'album'];
        fields.forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field.value.trim() !== '') {
                field.value = capitalizeWords(field.value.trim());
            }
        });
    }
});