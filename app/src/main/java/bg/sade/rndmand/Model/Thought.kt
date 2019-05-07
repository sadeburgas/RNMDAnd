package bg.sade.rndmand.Model

import java.util.*


data class Thought constructor(val username: String, val timestamp: Date?, val thoughtTxt: String,
                               val num_Likes: Int? , val NumComments: Int?, val documentId: String)