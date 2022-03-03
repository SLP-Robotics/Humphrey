@echo off

pip install yt-dlp
yt-dlp --max-filesize 8m --playlist-end 1 -r 5m -x --audio-format mp3 --format bestaudio https://www.youtube.com/watch?v=z3ZiVn5L9vM -o "music.%(ext)s"