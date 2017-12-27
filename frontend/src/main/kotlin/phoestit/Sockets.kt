package phoestit

external fun require(module:String): dynamic

val openSocket = require("socket.io-client")
val socket: dynamic = openSocket("http://localhost:3000")
