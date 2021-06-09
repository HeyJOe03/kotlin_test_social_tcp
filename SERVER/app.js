const app = require('express')()
const http = require('http').Server(app)
const io = require('socket.io')(http)
const ArrayList = require('arraylist')

function User(username,address){
    this.username = username
    this.address = address
}

let users = new ArrayList()

app.get('/',(req,res) => {
    res.sendFile('index.html',{root: __dirname + '/HTML'})
})

io.on('connection',(socket) => {


    //console.log('new user has connected: ' + socket.id)

    //io.to(socket.id).emit('message',{sender:"server",to:socket.id,message:"hello client"})

    socket.on('join_chat', (username) => {
        users.add(new User(username,socket.id))
        io.to(socket.id).emit('message',{"sender": "server","to":socket.id,"message":"your socket: " + socket.id})
    })

    socket.on('message', (data) => {
        console.log(data)
    })

    socket.on('disconnect', () => {
        console.log(socket.id + " left the chat")
    })


})

http.listen(process.env.PORT || 3000, () => {
    console.log(`> Server listening on port: ${process.env.PORT || 3000}`)
})