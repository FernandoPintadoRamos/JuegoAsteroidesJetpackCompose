# Cambios dentro de Asteroids-for-jetpackCompose

## Modo dificil (Cambio 1)
### ¿Qué hace?
Este cambio implemente un checkbox que al ser checkeado cambia la forma en la que se disparan los disparos. 
Una vez es checkeado el jugador solo será capaz de lanzar un disparo a la vez.

### ¿Cómo se hace?
Primero he añadido una variable de tipo booleana para que compruebe si es true o false el modo dificil.
``` kotlin
val modoDificil = remember { mutableStateOf(false) }
```

Para que se pueda hacer de manera visual el cambio a modo dificil añado un checkbox que cambie el valor del booleano.
``` kotlin
Checkbox(
  checked = modoDificil.value,
  onCheckedChange = {
    modoDificil.value = it
  },
  enabled = e.value,
)
```
A su vez he implementado que el checkbox quede deshabilitado una vez el juego esté iniciado.</br>
Esto se hace inicializando una variable llamada "e" que cambia segun el juego este pausado o corriendo.</br>
Ese cambio se realiza en el bucle donde se hace el update del juego comprobando si el juego ha parado o no.
``` kotlin
while (true) {
  withFrameNanos {
    game.update(it, modoDificil.value)
    e.value = game.gameState == GameState.STOPPED
    }
}
```
Cuando esa variable cambia el checbox setea su característica "enabled" en base a esta variable.
El parametro se le pasa a la funcion de actualizar el juego y se comprueba en este codigo.
``` kotlin
if (modoD){
  if (bullets.count() > 1) {
    gameObjects.remove(bullets.first())
  }
}else{
  if (bullets.count() > 3) {
    gameObjects.remove(bullets.first())
  }
}
```

## Cambio de color de la nave (Cambio 2)
### ¿Qué hace?
Implementa tres checkbox dentro de el menu drawer donde puedes elegir entre tres colores para la nave.
1. Variables:

### ¿Cómo se hace?
``` kotlin
var Ae = remember {mutableStateOf(false)}
var Re = remember {mutableStateOf(false)}
var Ve = remember {mutableStateOf(false)}
```
3. Checkboxes:
- CheckBox para el color Azul.
``` kotlin
Checkbox(
  checked = Ae.value,
  onCheckedChange = {
    Ae.value = it
    Ve.value = false
    Re.value = false
  },
  enabled = e.value,
)
```
- CheckBox para el color Verde.
``` kotlin
Checkbox(
  checked = Ve.value,
  onCheckedChange = {
    Ve.value = it
    Ae.value = false
    Re.value = false
  },
  enabled = e.value,
)
```
- CheckBox para el color Rojo.
``` kotlin
Checkbox(
  checked = Re.value,
  onCheckedChange = {
    Re.value = it
    Ae.value = false
    Ve.value = false
   },
  enabled = e.value,
)
```
Cada checkbox cambiar el valor del booleano asignado a cada checkbox a true y los demoas a false.</br>
En estos checkbox tambien se implemente el valor "e" para que queden unabled una vez se inicie el juego.</br>

Para que el valor del color cambie se crear una variable que maneje que color es el seleccionado antes de empezar el juego.
``` kotlin
var c: MutableState<Color> = remember { mutableStateOf(Color.White) }
```

Este valor cambia de la seiguiente manera:
``` kotlin
when {
  Ae.value -> c.value = Color.Blue
  Re.value -> c.value = Color.Red
  Ve.value -> c.value = Color.Green
  else -> c.value = Color.White
}
```
## Cambio en como se ven los asteroides (Cambio 3)
### ¿Qué hace?
Cambia la imagen que se muestra en los asteroides. </br>

### ¿Cómo se hace?
Este cambio se realiza en la funcion que genera los asteroides.
``` kotlin
Box(
  Modifier
    .offset(asteroidData.xOffset, asteroidData.yOffset)
    .size(asteroidSize)
    .rotate(asteroidData.angle.toFloat())
    .clip(CircleShape)
    .background(Color(0, 0, 30))
){
  Image(bitmap = imageFromResource("f1.jpg"),
    "image",
  )
}
```
Se le añade que dibuje una imagen en el box que muestre la imagen.
## Cambio de fondo (Cambio 4)
### ¿Qué hace?
Cambia el fondo que se muestra en el juego
### ¿Cómo se hace?
Se añade una imagen al box que conforma el fondo del juego.
``` kotlin
Image(
  bitmap = imageFromResource("fondo.jpg"),
  "image"
)
```

## Adición de menu Drawer (Cambio 5)
### ¿Qué hace?
Se añade un componente Scaffold que contiene a toda la pantalla.
### ¿Cómo se hace?
Se añade el Scaffold con los contenidos separados en los que son del menu y los que son de la pantalla.
``` kotlin
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
    //Contenido del menu
  },
  content = {
    //Contenido de la pantalla
  },
  bottomBar = { 
    BottomAppBar(backgroundColor = materialBlue700) { Text("") } 
  }
)
```
Dentro de este encontramos los checkboxes que manejan el color de la nave.
