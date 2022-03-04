import androidx.compose.desktop.Window
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


fun main() = Window(size = IntSize(800, 900), title = "Asteroids for Desktop") {
    val game = remember { Game() }
    var c: MutableState<Color> = remember { mutableStateOf(Color.White) }
    var e = remember { mutableStateOf(true) }
    val density = LocalDensity.current
    val modoDificil = remember { mutableStateOf(false) }



    var text = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it, modoDificil.value)
                e.value = game.gameState == GameState.STOPPED
            }
        }
    }

    val materialBlue700 = Color(0xFF1976D2)
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))

    /*
    Añado un menu que se abre deslizando el raton pulsando click izquierdo hacia la derecha
     */
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("Asteroid Compose") }, backgroundColor = materialBlue700) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Text("X")
            }
        },
        drawerContent = {
            //Inicializo variables que comprueban que color elige el usuario.
            var Ae = remember {mutableStateOf(false)}
            var Re = remember {mutableStateOf(false)}
            var Ve = remember {mutableStateOf(false)}
            /*
            CheckBoxs para que el usuario elija el color que quiere.
             */
            //CB Azul
            Text(
                text = "Nave azul",
                color = Color.Black
            )
            Checkbox(
                checked = Ae.value,
                onCheckedChange = {
                    Ae.value = it
                    Ve.value = false
                    Re.value = false
                },
                enabled = e.value,
                )

            //CB Verde
            Text(
                text = "Nave verde",
                color = Color.Black
            )
            Checkbox(
                checked = Ve.value,
                onCheckedChange = {
                    Ve.value = it
                    Ae.value = false
                    Re.value = false

                },
                enabled = e.value,
                )

            //CB Rojo
            Text(
                text = "Nave roja",
                color = Color.Black
            )
            Checkbox(
                checked = Re.value,
                onCheckedChange = {
                    Re.value = it
                    Ae.value = false
                    Ve.value = false
                },
                enabled = e.value,
                )

            when {
                Ae.value -> c.value = Color.Blue
                Re.value -> c.value = Color.Red
                Ve.value -> c.value = Color.Green
                else -> c.value = Color.White
            }
        },
        content = {
            Column(modifier = Modifier.background(Color(51, 153, 255)).fillMaxHeight()) {


                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {


                    Button({

                        game.startGame(modoDificil.value, c.value)
                        e.value = false
                    }) {
                        Text("Play")
                    }



                    Text(
                        text = "Modo dificil",
                        color = Color.White
                    )
                    // CheckBox Modo dificil
                    /*
                    Con este CheckBox se elige la modalidad de juego
                    Se activa el modo dificil si el checkBox está seleccionado
                     */
                    Checkbox(
                        checked = modoDificil.value,
                        onCheckedChange = {
                            modoDificil.value = it

                        },
                        enabled = e.value,
                    )
                    Text(
                        game.gameStatus,
                        modifier = Modifier.align(Alignment.CenterVertically).padding(horizontal = 16.dp),
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .aspectRatio(1.0f)
                        .background(Color(0, 0, 30))
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    //Se añade una imagen al fondo
                    Image(
                        bitmap = imageFromResource("fondo.jpg"),
                        "image"
                    )
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .clipToBounds()
                        .pointerMoveFilter(onMove = {
                            with(density) {
                                game.targetLocation = DpOffset(it.x.toDp(), it.y.toDp())
                            }
                            false
                        })
                        .clickable() {
                            game.ship.fire(game)
                        }
                        .onSizeChanged {
                            with(density) {
                                game.width = it.width.toDp()
                                game.height = it.height.toDp()
                            }
                        }) {

                        game.gameObjects.forEach {
                            when (it) {
                                is ShipData -> Ship(it)
                                is BulletData -> Bullet(it)
                                is AsteroidData -> Asteroid(it)
                            }
                        }
                    }
                }
            }
        },
        bottomBar = { BottomAppBar(backgroundColor = materialBlue700) { Text("") } }
    )


}