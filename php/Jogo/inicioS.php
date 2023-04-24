<!DOCTYPE html>
<html>
    <head>
    <link href='https://fonts.googleapis.com/css?family=Open Sans' rel='stylesheet'>
    <style>
        a {
        font-family: 'Open Sans';
        font-size: 22px;
        }
        form > table{
            background-color: rgb(233, 233, 233);
            border-radius: 20px 20px;
            border-collapse:collapse;
            position:absolute;
            top: 1vh;
            left: 40%;
            -webkit-box-shadow: -8px 0px 109px -7px rgba(0,0,0,0.75);
            -moz-box-shadow: -8px 0px 109px -7px rgba(0,0,0,0.75);
            box-shadow: -8px 0px 109px -7px rgba(0,0,0,0.75);
            animation: animation 10s linear infinite;
            animation-duration: 10s;           
        }
        form > table th{
            color: white;
            font-size: 1vmin;
        }

        form > table tr:first-child{
            background-color: black;
        }

        form > table td{
            padding: 0.2vmin;
            font-size: 2vmin;
            font-family: 'Roboto', sans-serif;
            border-radius: 20px 20px 20px 20px;
        }

        
        input{
            border: none;
            height: 40px;
            width: 100%;
            font-size: 80%;
            background-color:rgb(252, 252, 252);

        }

    </style>
    </head>
    <body style="background-color:#E8E8E8; margin: -1px">
        <header style="background-color: #ABB9ED">
            <img src="./Logo.png" width="290vw" style="margin-left: 42.5vw"/>
        </header>
            <form action="inicioS.php" method="get">
                <table style="position:absolute; top: 40%; left: 35%; font-size: 1.5vmin; height: 2vh; width: 30vw;">
                <tr>
                    <th style="font-size: 3vmin;  padding: 3vmin;">
                        INICIO DE SESION
                    </th>
                </tr>
                <tr>
                    <td style="padding: 0.1vmin; font-size: 3vmin; padding-left: 1.5vmin; padding-top: 1vmin; padding-right: 1.3vmin">
                        <input type="text" id="usuario" name="usuario" placeholder="usuario"/>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0.1vmin; font-size: 3vmin; padding-left: 1.5vmin; padding-top: 1vmin; padding-right: 1.3vmin">
                        <input type="password" id="contraseña" name="contraseña" placeholder="contraseña"/>
                    </td>
                </tr>
                <tr>
                    <td style="padding: 0.1vmin; font-size: 3vmin; padding-left: 1.5vmin; padding-top: 1vmin; padding-right: 1.3vmin;">
                        <input type="submit" id="envio" name="envio" value="Iniciar Sesion" style="background-color:#6BF2DC; color:white; font-size:90%"/>
                    </td>
                </tr>
                </table>
                <?php
                    if(isset($_GET["usuario"])== true && isset($_GET["contraseña"])==true){
                        function phpAlert($msg) {
                            echo "
                            <html>
                            <head>
                            <script src='https://unpkg.com/sweetalert/dist/sweetalert.min.js'></script>
                            </head>
                            <body>
                            <script>
                                swal('Vaya!', '$msg', 'warning');
                            </script>
                            </body>
                        </html>";
                        }

                        $encontrado = false;
                        # Metemos en una variable el nombre de usuario recibido del formulario 
                            $nombreUsuario = $_GET["usuario"];

                            $nuevaURL = 'Link-Evento';
                        # Metemos en una variable la contraseña recibida del formulario

                            $contraseña = $_GET["contraseña"];
                            
                        # Nos conectamos a la base de datos
                            $nombre = mysqli_connect("localhost", "Android", "android", "jogo");

                        # Elegimos la base de datos a la que nos conectamos
    
                        
                        # Variable donde guardamos la consulta a realizar.
                            $sql = "SELECT nombre, contraseña FROM usuario";

                        # Realizamos una consulta a la base de datos
                            $consulta = mysqli_query($nombre, $sql);
                        
                        # Nos colocamos en la primera fila de la base de datos
                            $numeroV = mysqli_num_rows($consulta);

                        # Recorremos las filas de la consulta
                            for($i = 0; $i < $numeroV; $i++){

                                # Nos colocamos en la primera fila
                                mysqli_data_seek($consulta,$i);

                                # Pasamos las columnas de la primera fila a un array
                                $extraido = mysqli_fetch_array($consulta);

                                # Si el valor de la primera columna es igual al nombre de usario introducido, alertamos
                                if($extraido['nombre'] == $nombreUsuario && $extraido['contraseña'] == $contraseña){
                                    $encontrado = true;
                                }
                            }
                            if($encontrado == true){
                                echo "
                                <script>
                                window.location.replace('Link-Evento.php?usuario=$nombreUsuario&idEvento=1');</script>";
                            }
                            else{
                                phpAlert('Usuario o contraseña incorrecto');
                            }
                    }
                ?>
            </form>
    </body>
</html> 

