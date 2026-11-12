This is a simple Server Menu plugin that adds a custom **Server Menu** button directly to the ESC (Game) menu.

<img width="800" height="450" alt="Server Menu Preview" src="https://github.com/user-attachments/assets/5367bf5f-2e86-4753-87cf-663fbc2bcd53" />

## ⚙️ Configuration

1. Open your server files.
2. Open the `plugins` folder.
3. Open the `ServerMenu` folder.
4. Choose how you want to set up your menu:
   - **`menu.yml`:** Open this file to add standard buttons to the menu.
   - **`Dialog` folder *(Optional)*:** Add dialog files here to create menus with more advanced features.
5. Follow the **`menu.yml`** structure below:
   - `title`: `"(title name)"` — Sets the title of the menu.
   - `columns`: `(number)` — Sets the number of columns in the menu layout.
   - `buttons`: — List of buttons to display:
     - `label`: `"(button name)"` — The text displayed on the button.
     - `icon`: `"(icon sprite)"` — The icon displayed on the button.
     - `command`: `"(your /command)"` *(Optional)* — Runs a command when clicked.
     - `open`: `"(dialog menu name)"` *(Optional)* — Opens a custom dialog menu.

### Example `menu.yml`

```yaml
title: "Server Menu"
columns: 2
buttons:
  - label: "Shop"
    icon: "sprite:block/emerald_block"
    command: "/shop"
  - label: "Spawn"
    icon: "sprite:item/compass"
    command: "/spawn"
```
