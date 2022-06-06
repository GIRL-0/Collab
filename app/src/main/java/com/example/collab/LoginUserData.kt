package com.example.collab

data class LoginUserData(var field:String, var intro:String, var name:String, var rating:String, var team: Array<String>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginUserData

        if (field != other.field) return false
        if (intro != other.intro) return false
        if (name != other.name) return false
        if (rating != other.rating) return false
        if (!team.contentEquals(other.team)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + intro.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + rating.hashCode()
        result = 31 * result + team.contentHashCode()
        return result
    }
}