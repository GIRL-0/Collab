package com.example.collab

data class TeamProject(var master:String, var teamName:String, var teamSubject:String)
{
    constructor():this("noinfo","noinfo","noinfo")
}