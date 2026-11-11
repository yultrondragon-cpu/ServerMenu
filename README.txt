ServerMenu - Paper plugin (ESC menu button via Dialogs)

Build:   gradle build   -> build/libs/ServerMenu.jar
Install: copy the jar to your server's plugins/ folder and start the server.

On first start the plugin creates (empty, nothing premade):
  plugins/ServerMenu/menu.yml     (the button on the ESC menu)
  plugins/ServerMenu/dialogs/     (put extra menus here, one .yml = one dialog)

Add buttons yourself, then restart (dialogs load at startup, /reload won't work).
A menu with no buttons is skipped; the ESC button appears once menu.yml has a button.

Button:
  - label: "<green>My Button"
    icon: "sprite:item/diamond_sword"
    command: "/spawn"          (or open: "dialogname")

Icon types: sprite:item/<name> | sprite:block/<name> | head:<player> | text:<unicode>

Not compiled against 26.2 - check Paper API version and class names if the build errors.

Build without installing anything:
  1. Create a GitHub repo and upload all files from this folder (keep the .github folder).
  2. Open the Actions tab -> Build -> wait for the green check.
  3. Open the finished run -> Artifacts -> download ServerMenu (contains ServerMenu.jar).
