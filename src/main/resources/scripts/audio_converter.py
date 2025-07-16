import sys
import subprocess

file_url = sys.argv[1]
output_file = sys.argv[2]

command = [ 
    "yt-dlp", 
    "--extract-audio", 
    "--audio-format", "mp3", 
    "-o", output_file, 
    file_url
]

subprocess.run(command, check=True)