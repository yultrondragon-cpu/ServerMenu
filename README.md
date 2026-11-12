This is a simple Server Menu plugin that adds an ESC menu button.

<img width="800" height="450" alt="2026-11-1216-02-13-ezgif com-video-to-gif-converter" src="https://github.com/user-attachments/assets/5367bf5f-2e86-4753-87cf-663fbc2bcd53" />

<h1><b>CONFIGURATION</b></h1>
1. Open server files 
2. Open server plugin folder
3. Open ServerMenu Folder
4. Open menu.yml to add buttons in the menu
                    Or
  Dialog folder is to add dialog which will menu like the opened but with more feature(optional)
5. Menu.yml Structure:- 
  - title: "(title name)" for the title 
  - columns: (number)
  - buttons: (to add buttons)
  - - label: "(name of the button)"
  - icon: "(to add icons)"
  - command: "(your /command)" (this runs any command)(optional)
  - open: "(dialog menu name)" (opens dialog menu) (optional)

  <b>Example</b>
 
   title: "Server Menu"
   columns: 2
   buttons:
     - label: "Shop"
       icon: "sprite:block/emerald_block"
       command: "/shop"
