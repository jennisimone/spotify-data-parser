package uk.co.jennisimone.Spotify.Data.Parser

import com.google.gson.Gson
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileReader
import java.io.IOException
import java.nio.file.Paths


@RestController
@RequestMapping("/api/parse")
class Controller {

    @GetMapping(path = ["/artists/{year}"])
    fun findArtists(@PathVariable year: String): List<ArtistRecord> {
        val history: Array<ListeningHistory> = listeningHistories(year)
        return history
            .groupBy { it.master_metadata_album_artist_name }
            .map { it.key to it.value }.sortedBy { it.second.size }.reversed().toMap()
            .map { entry -> ArtistRecord(artist = entry.key, timesPlayed = entry.value.size) }
            .onEachIndexed{ index, entry -> entry.position = index + 1}
    }

    @GetMapping(path = ["/artists/tracks/{year}"])
    fun findArtistTracks(@PathVariable year: String): List<ArtistTracksRecord> {
        val history: Array<ListeningHistory> = listeningHistories(year)
        return history
            .groupBy { it.master_metadata_album_artist_name }
            .map { it.key to it.value }.sortedBy { it.second.size }.reversed().toMap()
            .map { entry ->
                ArtistTracksRecord(
                    artist = entry.key,
                    songs = entry.value.groupBy { it.master_metadata_track_name }.map { it.key to it.value }
                        .sortedBy { it.second.size }.reversed()
                        .map { it.first }.toSet(),
                    timesPlayed = entry.value.size
                )
            }.onEachIndexed{ index, entry -> entry.position = index + 1}
    }

    @GetMapping(path = ["/artists/albums/{year}"])
    fun findArtistAlbums(@PathVariable year: String): List<ArtistAlbumsRecord> {
        val history: Array<ListeningHistory> = listeningHistories(year)
        return history
            .groupBy { it.master_metadata_album_artist_name }
            .map { it.key to it.value }.sortedBy { it.second.size }.reversed().toMap()
            .map { entry ->
                ArtistAlbumsRecord(
                    artist = entry.key,
                    albums = entry.value.groupBy { it.master_metadata_album_album_name }.map { it.key to it.value }
                        .sortedBy { it.second.size }.reversed()
                        .map { it.first }.toSet(),
                    timesPlayed = entry.value.size
                )
            }.onEachIndexed{ index, entry -> entry.position = index + 1}
    }

    @GetMapping(path = ["/tracks/{year}"])
    fun findTracks(@PathVariable year: String): List<TrackRecord> {
        val history: Array<ListeningHistory> = listeningHistories(year)
        return history
            .groupBy { it.master_metadata_track_name }
            .map { it.key to it.value }.sortedBy { it.second.size }.reversed().toMap()
            .map { entry -> TrackRecord(
                artist = entry.value[0].master_metadata_album_artist_name,
                track = entry.key,
                timesPlayed = entry.value.size
            ) }
            .onEachIndexed{ index, entry -> entry.position = index + 1}
    }

    @GetMapping(path = ["/albums/{year}"])
    fun findAlbums(@PathVariable year: String): List<AlbumRecord> {
        val history: Array<ListeningHistory> = listeningHistories(year)
        return history
            .groupBy { it.master_metadata_album_album_name }
            .map { it.key to it.value }.sortedBy { it.second.size }.reversed().toMap()
            .map { entry -> AlbumRecord(
                artist = entry.value[0].master_metadata_album_artist_name,
                album = entry.key,
                timesPlayed = entry.value.size
            ) }
            .onEachIndexed{ index, entry -> entry.position = index + 1}
    }

    private fun listeningHistories(
        year: String,
    ): Array<ListeningHistory> {
        var history = emptyArray<ListeningHistory>()
        val gson = Gson()
        println(Paths.get("").toAbsolutePath());

        try {
            FileReader(linkYearToFile(year)).use { reader ->
                history = gson.fromJson(reader, Array<ListeningHistory>::class.java)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return history
    }

    private fun linkYearToFile(year: String): String =
        when (year) {
            "2010" -> "src/main/resources/Streaming_History_Audio_2010-2015_0.json"
            "2015" -> "src/main/resources/Streaming_History_Audio_2015-2016_1.json"
            "2016" -> "src/main/resources/Streaming_History_Audio_2016-2018_2.json"
            "2018" -> "src/main/resources/Streaming_History_Audio_2018-2020_3.json"
            "2020" -> "src/main/resources/Streaming_History_Audio_2020-2022_4.json"
            "2022" -> "src/main/resources/Streaming_History_Audio_2022-2024_5.json"
            else -> "src/main/resources/Streaming_History_Audio_2022-2024_5.json"
        }
}

data class ArtistRecord(var position: Int = 0, val artist: String?, val timesPlayed: Int)

data class ArtistTracksRecord(
    var position: Int = 0,
    val artist: String?,
    val songs: Set<String?>,
    val timesPlayed: Int
)

data class ArtistAlbumsRecord(
    var position: Int = 0,
    val artist: String?,
    val albums: Set<String?>,
    val timesPlayed: Int
)

data class TrackRecord(var position: Int = 0, val artist: String?, val track: String?, val timesPlayed: Int)

data class AlbumRecord(var position: Int = 0, val artist: String?, val album: String?, val timesPlayed: Int)