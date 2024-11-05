function generateMovieTitles(count) {
    const adjectives = ["Mysterious", "Forgotten", "Eternal", "Lost", "Dark", "Invisible", "Haunted", "Secret", "Broken", "Dangerous"];
    const genres = ["Adventure", "Fantasy", "Horror", "Thriller", "Comedy", "Drama", "Sci-Fi", "Romance", "Western", "Mystery"];
    const nouns = ["Journey", "Legacy", "Dream", "Labyrinth", "Fortress", "Quest", "Empire", "Night", "Shadow", "Revenge"];

    const titles = [];

    for (let i = 0; i < count; i++) {
        const adjective = adjectives[Math.floor(Math.random() * adjectives.length)];
        const genre = genres[Math.floor(Math.random() * genres.length)];
        const noun = nouns[Math.floor(Math.random() * nouns.length)];

        // Create a movie title by combining the random words
        const title = `${adjective} ${genre} of the ${noun}`;
        titles.push(title);
    }

    return titles;
}

function generateRandomDates(count, startYear = 1960, endYear = 2024) {
    const dates = [];

    for (let i = 0; i < count; i++) {
        // Generate a random date
        const randomDate = new Date(
            Math.floor(Math.random() * (endYear - startYear + 1) + startYear),
            Math.floor(Math.random() * 12), // Month (0-11)
            Math.floor(Math.random() * 31) + 1 // Day (1-31)
        );

        // Format the date to dd-MM-yyyy
        const formattedDate = `${String(randomDate.getDate()).padStart(2, '0')}-${String(randomDate.getMonth() + 1).padStart(2, '0')}-${randomDate.getFullYear()}`;
        dates.push(formattedDate);
    }

    return dates;
}

function generateRandomNames(count) {
    const firstNames = ["John", "Jane", "Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona", "George", "Hannah"];
    const lastNames = ["Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"];

    const names = [];

    for (let i = 0; i < count; i++) {
        // Generate a random first name and last name
        const firstName = firstNames[Math.floor(Math.random() * firstNames.length)];
        const lastName = lastNames[Math.floor(Math.random() * lastNames.length)];

        // Create a full name
        const fullName = `${firstName} ${lastName}`;
        names.push(fullName);
    }

    return names;
}


async function createMedia(movieTitle, movieReleaseDate) {
    console.log(`${movieTitle} - ${movieReleaseDate}`)
    await fetch("http://localhost:9999/media", {
        method: "POST",
        body: JSON.stringify({
            "title": movieTitle,
            "releaseDate": movieReleaseDate,
            "mediaType": Math.floor(Math.random() * 2) === 0 ? "TV_SHOW" : "MOVIE"
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    });
}

async function createPerson(personName, age, gender) {
    console.log(`${personName} - ${age} - ${gender}`)
    await fetch("http://localhost:9999/user", {
        method: "POST",
        body: JSON.stringify({
            "name": personName,
            "age": age,
            "gender": gender
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    });
}

async function createRating(userId, mediaId, rate) {
    console.log(`${userId} - ${mediaId} - ${rate}`)
    await fetch("http://localhost:9999/mediarate", {
        method: "POST",
        body: JSON.stringify({
            "mediaId": mediaId,
            "userId": userId,
            "rate": rate
        }),
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    })
}

const NUMBER_OF_PERSON = 10000;
const NUMBER_OF_MOVIES = 10000;
const NUMBER_OF_RATINGS = 50000;

async function createMedias() {
    const movieTitles = generateMovieTitles(NUMBER_OF_MOVIES);
    const randomDates = generateRandomDates(NUMBER_OF_MOVIES);
    for (let i = 0; i < NUMBER_OF_MOVIES; i++) {
        await createMedia(movieTitles[i], randomDates[i])
    }
}

async function createPersons() {
    const randomNames = generateRandomNames(NUMBER_OF_PERSON);
    for (let i = 0; i < NUMBER_OF_PERSON; i++) {

        await createPerson(randomNames[i], Math.floor(Math.random() * (80 - 1) + 1), Math.floor(Math.random() * 2) === 0 ? "MALE" : "FEMALE")
    }
}


async function createRatings() {
    for (let i = 0; i < NUMBER_OF_RATINGS; i++) {
    setTimeout(function timer() {
        createRating(
                    Math.floor(Math.random() * (NUMBER_OF_PERSON - 1) + 1),
                    Math.floor(Math.random() * (NUMBER_OF_MOVIES - 1) + 1),
                    Math.random() * 10
                )
        }, i * 10)
    }
}

(async () => {
    // await createMedias();
    // await createPersons();
    // await createRatings()
    createRatings()
})();
