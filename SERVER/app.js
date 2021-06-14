const app = require('express')()
const http = require('http').Server(app)
const io = require('socket.io')(http)
const ArrayList = require('arraylist')

class User{
    constructor(username,address){
        this.username = username
        this.address = address
    }
}

let users = new ArrayList()

app.get('/',(req,res) => {
    res.sendFile('index.html',{root: __dirname + '/HTML'})
})

io.on('connection',(socket) => {

    let element_me
    console.log('new user has connected: ' + socket.id)

    //io.to(socket.id).emit('message',{sender:"server",to:socket.id,message:"hello client"})


    socket.on('join_chat', (username) => {
        element_me = new User(username,socket.id)
        users.add(element_me)

        //TODO: aggiungere controllo sull'unicità dell'id

        io.to(socket.id).emit('message',{"sender": "server","to":socket.id,"message":"your socket: " + socket.id})
    })

    socket.on('message', ({sender,to,message}) => {
        console.log({sender,to,message})

        // toSocket = users.find(e => {
        //     return e.username == to
        // })[0].address

        // console.log(toSocket)

        // io.to(toSocket).emit('message',{sender,to,message})

        socket.broadcast.emit("message",{sender,to,message})
    })

    socket.on('disconnect', () => {
        console.log(socket.id + " left the chat")

        //FIXME: to check

        io.emit('message',{sender:"server",to:"all",message:`user: ${socket.id} left the chat`})

        users.removeElement(element_me)
    })


})

http.listen(process.env.PORT || 3000, () => {
    console.log(`> Server listening on port: ${process.env.PORT || 3000}`)
})