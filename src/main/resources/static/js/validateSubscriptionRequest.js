function validateSubscriptionRequest(form) {
    //Prevent requests for duplicate subscriptions at the frontend.

    //Get the title and album from the subscription request.
    const title = form.querySelector('input[name="songs[0].title"]').value;
    const album = form.querySelector('input[name="songs[0].album"]').value;

    //Check to see if we're already subscribed.
    const isDuplicate = subscribedSongs.some(song =>
        song.title === title && song.album === album
    );

    if (isDuplicate) {
        alert("Wow, you must LOVE this track! You’re already subscribed!");
        return false;
    }

    return true;
}