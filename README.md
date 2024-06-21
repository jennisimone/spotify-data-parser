# Spotify Data Parser

A simple springboot application made to parse the data you get back from spoitfy when requesting your listening history. 

To use this project, you will need to replace the existing streaming files in `resources` with your files, then check the names and years match up in `Controller.kt`.

You can then look at your Artist history, Track history and Album history for each file by running the application file and calling the following URLs:
- `localhost:8080/api/parse/artists/{year}`
- `localhost:8080/api/parse/tracks/{year}`
- `localhost:8080/api/parse/albums/{year}`

These will display your data in JSON format, with the requested fields in order of amount listened. Artists also groups the tracks for ease, and Tracks and Albums both tell you who the artist is. It's worth noting though that compilation albums will just display the artist for the first track rather than various.
