package uk.co.jennisimone.Spotify.Data.Parser

data class ListeningHistory(
    val conn_country: String,
    val episode_name: Any,
    val episode_show_name: Any,
    val incognito_mode: Any,
    val ip_addr_decrypted: String,
    val master_metadata_album_album_name: String,
    val master_metadata_album_artist_name: String,
    val master_metadata_track_name: String,
    val ms_played: Int,
    val offline: Any,
    val offline_timestamp: Any,
    val platform: String,
    val reason_end: String,
    val reason_start: String,
    val shuffle: Boolean,
    val skipped: Boolean,
    val spotify_episode_uri: Any,
    val spotify_track_uri: String,
    val ts: String,
    val user_agent_decrypted: Any,
    val username: String
)