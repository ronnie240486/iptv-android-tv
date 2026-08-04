# IPTV TV App — Android TV em Kotlin

App IPTV nativo para Android TV, construído com Kotlin + Jetpack Compose for TV.

## Como abrir no Android Studio

1. Abra o **Android Studio**
2. Clique em **File → Open**
3. Navegue até: `C:\Users\Meus Documentos\.gemini\antigravity\scratch\tv-iptv-app`
4. Clique **OK**
5. Aguarde o Gradle sincronizar (primeira vez pode demorar 5 minutos)
6. Clique no ▶️ **Run** para gerar o APK

## Como instalar na TV

Após compilar, o APK ficará em:
`app/build/outputs/apk/debug/app-debug.apk`

Transfira para a TV via:
- **ADB**: `adb connect <IP_TV>` → `adb install app-debug.apk`
- **Pendrive**: copie o APK e instale via gerenciador de arquivos
- **Compartilhamento de rede**: copie via Wi-Fi

## Funcionalidades

- 📺 TV ao Vivo (Xtream Codes + M3U)
- 🎬 Filmes (VOD)
- 📺 Séries com episódios
- 📻 Rádios
- 🔍 Busca global + Busca por voz
- 📅 EPG (Guia de Programação)
- 🎲 Roleta de filmes com animação
- 🤖 Cine IA com sugestões por gênero
- ⚙️ Configurações
- Player nativo ExoPlayer (HLS, DASH, MP4)

## Tecnologias

- Kotlin 2.0
- Jetpack Compose for TV
- ExoPlayer / Media3
- Retrofit + OkHttp
- Coil (imagens)
- DataStore (preferências)
- Room Database (cache)
