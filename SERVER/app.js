const app = require('express')()    //require and run express()
const http = require('http').Server(app) //set http server
const io = require('socket.io')(http) //io = socketio(http)
const ArrayList = require('arraylist') //just for userfull functions over vectors

class User{
    constructor(username,address){
        this.username = username
        this.address = address
    }
}

let users = new ArrayList() //array of Users

app.get('/',(req,res) => { //  http://192.168.x.x/
    res.sendFile('index.html',{root: __dirname + '/HTML'})
})


/* 
EVENTS:
"message"
"join_chat"
*/

io.on('connection',(socket) => { //when a user connect:

    let element_me // Bitch variable, just to assign and create the new User
    console.log('new user has connected: ' + socket.id) //write on console the socket of the user that has connected

    //io.to(socket.id).emit('message',{sender:"server",to:socket.id,message:"hello client"}) //answer to the client "hello"

    socket.on('join_chat', (username) => {
        element_me = new User(username,socket.id) //create the new User
        users.add(element_me) //add the user to the list

        //TODO: aggiungere controllo sull'unicità dell'id

        let json_to_emit = {"sender": "server","to":socket.id,"message":"your socket: " + socket.id} //the user will be the only who this message will be sent to
        io.to(socket.id).emit('message',json_to_emit)
    })

    socket.on('message', ({sender,to,message}) => {
        console.log({sender,to,message})

        toSocket = users.find(e => {
            return e.username == to
        })[0].address //toSocket = the address of the user that has the same socket as "to"

        console.log(toSocket)

        io.to(toSocket).emit('message',{sender,to,message}) //to send to a specific user
    })

    socket.on('disconnect', () => {
        console.log(socket.id + " left the chat")

        //FIXME: to check

        users.removeElement(element_me)
    })


})

http.listen(process.env.PORT || 3000, () => {
    console.log(`> Server listening on port: ${process.env.PORT || 3000}`)
})