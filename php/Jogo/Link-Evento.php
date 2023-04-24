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
            <a style="padding: 7vw; color: #3CE500">ASISTENCIA CONFIRMADA CORRECTAMENTE</a>            
        </div>
    </body>
</html>


<?php
    if((isset($_GET["idEvento"]) == TRUE) && (isset($_GET["usuario"]) == TRUE)){
        $idEvento = $_GET["idEvento"];
        $usuario = $_GET["usuario"];
        $nombreConexion = mysqli_connect("localhost", "Android", "android", "jogo");
        mysqli_query($nombreConexion, "INSERT INTO asistir_evento(nombre, id) VALUES ('$usuario','$idEvento')");
    }
    else{
        echo "
        <script>
        window.location.replace('inicioS.php?idEvento=1');</script>";
    }
?> 