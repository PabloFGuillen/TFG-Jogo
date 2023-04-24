<!DOCTYPE html>
<html>
    <head>
    <link href='https://fonts.googleapis.com/css?family=Open Sans' rel='stylesheet'>
    <style>
        a {
        font-family: 'Open Sans';
        font-size: 22px;
        }
    </style>
    </head>
    <body style="background-color:#E8E8E8; margin: -1px">
        <header style="background-color: #ABB9ED">
            <img src="./Logo.png" width="290vw" style="margin-left: 42.5vw"/>
        </header>
        <div style="background-color: #BEF8C4; margin-left: 30vw; margin-top: 20vh; width: 50vw; height: 15vmin; padding-top: 10vh">
            <a style="padding: 7vw; color: #3CE500">¡BIENVENIDO A JOGO!  Cuenta creada correctamente</a>            
        </div>
    </body>
</html>

<?php
    if(isset($_GET["usuario"]) == TRUE){
        $usuario = $_GET["usuario"];
        $nombreConexion = mysqli_connect("192.168.1.56", "Android", "android");
        mysqli_select_db($nombreConexion, "jogo");
        mysqli_query($nombreConexion, "UPDATE usuario SET validado = 1 WHERE nombre = '$usuario'");
    }
    else{
        
    }
?>

