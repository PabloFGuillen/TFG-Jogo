var mysql = require('mysql');
var conexion= mysql.createConnection({
    host : '192.168.1.51',
    database : 'jogo',
    user : 'Android',
    password : 'android',
});

conexion.connect(function(err) {
    if (err) {
        console.error('Error de conexion: ' + err.stack);
        return;
    }
    console.log('Conectado con el identificador ' + conexion.threadId);
});

conexion.query('UPDATE usuario SET validado = "1" WHERE nombre = "pablo"', function (error, results, fields) {
    if (error)
        throw error;

    results.forEach(result => {
        console.log(result);
    });
});