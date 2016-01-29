//imports

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

public class GUI extends javax.swing.JFrame {

    //timers needed to run various tasks
    Timer timer1, timer2, timer3;
    //tasks the timers run
    TimerTask txtupdate;
    TimerTask pupdate;
    TimerTask battleTimer, battleCheck;
    //the player
    Player p;
    //the enemy
    Enemy e;
    //for playing music
    Clip pagoda, hit, danger, levelup;
    //for the editing of lists
    DefaultListModel model;
    //hashmaps for keeping track of items
    HashMap<Integer, Item> map;
    HashMap<Integer, Item> temp;
    //keeps track of who's turn it is
    int turn = 0;
    //keeps track of what floor the player is on and their kills
    int floor = 0, kills = -1;
    //declaration for the choice a player makes later on in the game
    int choice;
    //used to check the current stats of the player
    int levelcheck, attackcheck, defensecheck, hpcheck;
    //used to set the player name and if they're buying or selling in the shop
    String nameplayer, shoptype;
    
    public GUI() {
        //sets the player's name at the intro
        nameplayer = JOptionPane.showInputDialog("Who are you?");
        p = new Player(nameplayer, 1, 0);
        initComponents();
        //makes it so the dialog box scrolls down automatically
        DefaultCaret caret = (DefaultCaret) txtevent.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        //try catch for loading music files
        try {
            AudioInputStream pagodamusic = AudioSystem.getAudioInputStream(
                    this.getClass().getResource("12 - Timeworn Pagoda.wav"));
            AudioInputStream hitsound = AudioSystem.getAudioInputStream(
                    this.getClass().getResource("Hit.wav"));
            AudioInputStream dangermusic = AudioSystem.getAudioInputStream(
                    this.getClass().getResource("13 - Danger.wav"));
            AudioInputStream levelupsound = AudioSystem.getAudioInputStream(
                    this.getClass().getResource("levelup.wav"));
            pagoda = AudioSystem.getClip();
            pagoda.open(pagodamusic);
            hit = AudioSystem.getClip();
            hit.open(hitsound);
            danger = AudioSystem.getClip();
            danger.open(dangermusic);
            levelup = AudioSystem.getClip();
            levelup.open(levelupsound);
        } catch (Exception ex) {
        }
        //starts the music
        pagoda.start();
        //loops the music a lot of time
        pagoda.loop((int) Math.pow(1000, 1000));
        //centres the window on the screen
        this.setLocationRelativeTo(null);
        //resizing the window messes with the GUI so its set to not resize
        this.setResizable(false);
        //making sure you can't attack before the battle starts
        btnattack.setEnabled(false);
        //checking the players current stats
        hpcheck = p.getPlayerCurrentHealth();
        levelcheck = p.getPlayerLevel();
        attackcheck = p.getPlayerAttack();
        defensecheck = p.getPlayerDefense();
        attackcheck = p.getPlayerAttack();
        defensecheck = p.getPlayerDefense();
        //configuring the various progress bars to work
        expbar.setMinimum(0);
        expbar.setStringPainted(true);
        hpbar.setMinimum(0);
        hpbar.setStringPainted(true);
        enemybar.setMinimum(0);
        enemybar.setStringPainted(true);
        enemybar.setString("");
        //initializing the timers
        timer1 = new Timer();
        timer2 = new Timer();
        timer3 = new Timer();
        //initializing the model
        model = new DefaultListModel();
        //making a temporary hashmap and duplicating it as the players map
        temp = new HashMap<Integer, Item>();
        temp.putAll(p.getHashMap());
        //map for the shop
        map = new HashMap<Integer, Item>();
        //adding items to the shop
        map.put(1, new Potion("Potion", 10, 50));
        map.put(2, new Sword("Sword", 3, 100));
        //used to update the dialog box
        txtupdate = new TimerTask() {
            @Override
            public void run() {
                int size = 0;
                //removes the last line of the box
                if (txtevent.getDocument().getLength() > size) {
                    int end = 0;
                    try {
                        end = txtevent.getLineEndOffset(0);
                    } catch (BadLocationException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    txtevent.replaceRange("", 0, end);
                }
                size = txtevent.getDocument().getLength();
            }
        };
        //updates various things in regards to the player
        pupdate = new TimerTask() {
            @Override
            public void run() {
                p.PlayerUpdate();
                //setting the various labels in the GUI
                lbllvl.setText(p.getPlayerName() + " LVL " + p.getPlayerLevel());
                lblgold.setText("$" + p.getPlayerGold());
                //Making sure the players hp and exp bars are up to date
                expbar.setMaximum(p.getNextLevel() - p.getPreviousLevel());
                expbar.setValue(p.getPlayerExperience() - p.getPreviousLevel());
                expbar.setString((p.getPlayerExperience() - p.getPreviousLevel()) + "/" + (p.getNextLevel() - p.getPreviousLevel()));
                hpbar.setMaximum(p.getPlayerHealth());
                hpbar.setValue(p.getPlayerCurrentHealth());
                hpbar.setString(p.getPlayerCurrentHealth() + "/" + p.getPlayerHealth());
                //enemy's bars are in a try catch, so when there is no enemy we don't get an error
                try {
                    enemybar.setMaximum(e.getEnemyHealth());
                    enemybar.setValue(e.getCurhealth());
                    enemybar.setString(e.getCurhealth() + "/" + e.getEnemyHealth());
                } catch (Exception e) {
                    
                }
                //runs when the player dies
                if (p.getPlayerCurrentHealth() < 1) {
                    //stops all timers
                    timer1.cancel();
                    timer2.cancel();
                    timer3.cancel();
                    //telling the player they have died
                    txtevent.append("\n" + p.getPlayerName() + " has been defeated by the " + e.getEnemyName());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    txtevent.append("\nThe game will now exit");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //program closes
                    System.exit(0);
                }
                //checks if the player has levelled up
                if (p.getPlayerLevel() > levelcheck) {
                    //plays level up sound
                    levelup.setFramePosition(0);
                    levelup.start();
                    //telling the player their stat increases
                    txtevent.append("\n" + p.getPlayerName() + " has leveled up!\n");
                    txtevent.append("Health has gone up " + ((p.getPlayerHealth()) - ((int) ((((p.getPlayerLevel() - 1) - 1) * ((p.getPlayerLevel()) - 1)) + 20))));
                    txtevent.append("\nAttack has gone up " + (p.getPlayerAttack() - attackcheck));
                    txtevent.append("\nDefense has gone up " + (p.getPlayerDefense() - defensecheck) + "\n");
                    //increases the players health
                    p.setCurrentPlayerHealth();
                    //sets the checks to the new statistics
                    levelcheck = p.getPlayerLevel();
                    attackcheck = p.getPlayerAttack();
                    defensecheck = p.getPlayerDefense();
                }
            }
        };
        //the timers run as long as the player is alive
        if (p.getPlayerCurrentHealth() >= 1) {
            timer1.scheduleAtFixedRate(txtupdate, 10000, 10000);
            timer1.scheduleAtFixedRate(pupdate, 10, 10);
        }
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtevent = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        lbllvl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        expbar = new javax.swing.JProgressBar();
        hpbar = new javax.swing.JProgressBar();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstshop = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        btnbuy = new javax.swing.JButton();
        btnsell = new javax.swing.JButton();
        lblgold = new javax.swing.JLabel();
        btnconfirm = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblenemy = new javax.swing.JLabel();
        background = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblenemyname = new javax.swing.JLabel();
        enemybar = new javax.swing.JProgressBar();
        lblfloor = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnattack = new javax.swing.JButton();
        btnbattle = new javax.swing.JButton();
        btnitem = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        devcons = new javax.swing.JMenuItem();
        mnuexit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnustats = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Deeper");

        txtevent.setEditable(false);
        txtevent.setColumns(20);
        txtevent.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtevent.setLineWrap(true);
        txtevent.setRows(5);
        txtevent.setAutoscrolls(false);
        txtevent.setFocusable(false);
        jScrollPane1.setViewportView(txtevent);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbllvl.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbllvl.setText("LVL ");

        jLabel2.setText("HP");

        jLabel3.setText("EXP");

        expbar.setBackground(new java.awt.Color(255, 255, 255));
        expbar.setForeground(new java.awt.Color(0, 0, 0));
        expbar.setBorderPainted(false);

        hpbar.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(expbar, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                            .addComponent(hpbar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(lbllvl))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbllvl)
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(hpbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expbar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setPreferredSize(new java.awt.Dimension(198, 220));

        jScrollPane2.setViewportView(lstshop);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("SHOP");

        btnbuy.setText("Buy");
        btnbuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbuyActionPerformed(evt);
            }
        });

        btnsell.setText("Sell");
        btnsell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsellActionPerformed(evt);
            }
        });

        lblgold.setIcon(new javax.swing.ImageIcon(getClass().getResource("/coins.png"))); // NOI18N
        lblgold.setText("$0.00");

        btnconfirm.setText("Confirm");
        btnconfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnconfirmActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblgold))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnbuy)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsell)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnconfirm)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblgold))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnbuy)
                    .addComponent(btnsell)
                    .addComponent(btnconfirm))
                .addGap(201, 201, 201))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(null);

        lblenemy.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblenemy.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.add(lblenemy);
        lblenemy.setBounds(0, 0, 430, 440);
        jPanel3.add(background);
        background.setBounds(0, 0, 0, 0);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lblenemyname.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lblfloor.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enemybar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lblenemyname, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(lblfloor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblenemyname, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                    .addComponent(lblfloor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(enemybar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnattack.setText("Attack");
        btnattack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnattackActionPerformed(evt);
            }
        });

        btnbattle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        btnbattle.setText("BATTLE!");
        btnbattle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnbattleActionPerformed(evt);
            }
        });

        btnitem.setText("Item");
        btnitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnitemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnbattle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(btnattack, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnitem, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnattack)
                    .addComponent(btnitem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnbattle, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu1.setText("File");

        devcons.setText("Developer Console");
        devcons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                devconsActionPerformed(evt);
            }
        });
        jMenu1.add(devcons);

        mnuexit.setText("Exit");
        mnuexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuexitActionPerformed(evt);
            }
        });
        jMenu1.add(mnuexit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Character");

        mnustats.setText("Stats");
        mnustats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnustatsActionPerformed(evt);
            }
        });
        jMenu2.add(mnustats);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnbattleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbattleActionPerformed
        //parameters for starting the battle
        if (kills % 10 != 0 || kills == -1 || floor == 0) {
            if (floor == 0) {
                floor++;
            }
            if (kills == -1) {
                kills = 0;
            }
            //starts the new music
            pagoda.stop();
            danger.start();
            danger.loop((int) Math.pow(1000, 1000));
            //making sure you can't use the shop in the dungeon
            btnbattle.setEnabled(false);
            btnbuy.setEnabled(false);
            btnsell.setEnabled(false);
            btnconfirm.setEnabled(false);
            lstshop.setEnabled(false);
            //getter for spawning the enemy
            getEnemy();
            //telling the player what enemy has appeared
            txtevent.append("\nA " + e.getEnemyName() + " has appeared!" + "\n");
            timer3.cancel();
            //starts the battle with the enemy
            battle(e);
            lblfloor.setText("Floor " + floor);
        } else {
            //when the player finishes the tenth floor they have the option to continue or go back to town
            if (floor >= 1) {
                choice = JOptionPane.showConfirmDialog(this, "Would you like to proceed to the next floor?", "Choose", JOptionPane.YES_NO_OPTION);
            }
            //player chose to continue and the battle starts again from where itr left off
            if (choice == 0) {
                floor++;
                timer3.cancel();
                btnbattle.setEnabled(false);
                getEnemy();
                txtevent.append("\nA " + e.getEnemyName() + " has appeared!" + "\n");
                battle(e);
                lblfloor.setText("Floor " + floor);
            }
            //player returns to the town
            if (choice == 1) {
                //heals the player fully
                p.setCurrentPlayerHealth();
                //sets the floor back to 0
                floor = 0;
                lblfloor.setText("");
                //cancels the battle timer
                timer3.cancel();
                btnbattle.setText("Battle!");
                //re-enabling all the shop buttons
                btnbattle.setEnabled(true);
                btnattack.setEnabled(false);
                btnitem.setEnabled(true);
                btnbuy.setEnabled(true);
                btnsell.setEnabled(true);
                btnconfirm.setEnabled(true);
                lstshop.setEnabled(true);
                //starts town music again
                danger.stop();
                pagoda.setFramePosition(0);
                pagoda.start();
                pagoda.loop((int) Math.pow(1000, 1000));
                //removes the enemies stats in the top
                lblenemy.setText("");
                enemybar.setString("");
            }
        }
    }//GEN-LAST:event_btnbattleActionPerformed

    //closes the program
    private void mnuexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuexitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuexitActionPerformed

    //everything to do with how the player attacks
    private void btnattackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnattackActionPerformed
       //runs if the player is alive
        if (p.getPlayerCurrentHealth() >= 1) {
            txtevent.append("\n" + p.getPlayerName() + " attacks!" + "\n");
            //plays hit sound
            hit.start();
            hit.setFramePosition(0);
            int temp;
            // damage calculation
            temp = ((int) ((Math.random() * (p.getPlayerAttack() - (p.getPlayerAttack() * 0.75)) + p.getPlayerAttack() * 0.75) + 0.50)) - e.getEnemyDefense();
            //ensures you always hit at least one damage
            if (temp < 1) {
                temp = 1;
            }
            //does the damage to the enemy
            e.setCurhealth(temp);
            txtevent.append(p.getPlayerName() + " dealt " + temp + " damage to the " + e.getEnemyName() + "\n");
            //goes to the enemies turn
            turn++;
            btnattack.setEnabled(false);
            btnitem.setEnabled(false);
        }
    }//GEN-LAST:event_btnattackActionPerformed

    //shows all the player's stats
    private void mnustatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnustatsActionPerformed
        String result = "";
        result += ("" + p.getPlayerName() + "\n");
        result += ("\nLevel: " + p.getPlayerLevel());
        result += ("\nExperience: " + (p.getPlayerExperience() - p.getPreviousLevel()));
        result += ("\nNext Level: " + ((p.getNextLevel() - p.getPreviousLevel()) - (p.getPlayerExperience() - p.getPreviousLevel())));
        result += ("\nTotal Experience: " + p.getPlayerExperience() + "\n");
        result += ("\nHealth: " + p.getPlayerHealth());
        result += ("\nAttack: " + p.getPlayerAttack());
        result += ("\nDefense: " + p.getPlayerDefense() + "\n");
        result += ("\nKills: " + kills);
        JOptionPane.showMessageDialog(this, result);
    }//GEN-LAST:event_mnustatsActionPerformed

    //the developer console, used to make sure I don't lose the game while testing
    private void devconsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_devconsActionPerformed
        String cons = JOptionPane.showInputDialog(this, "Developer Console");
        try {
            if (cons.equals("maxhealth")) {
                p.setCurrentPlayerHealth();
            }
            if (cons.equals("smite")) {
                e.setCurhealth(1000);
            }
            if (cons.equals("buffx10")) {
                p.addPlayerExperience(10);
            }
            if (cons.equals("buffx100")) {
                p.addPlayerExperience(100);
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_devconsActionPerformed

    private void btnbuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnbuyActionPerformed
        //sets the shop to the buy option
        shoptype = "buy";
        model.clear();
        //adds all the items you can buy
        for (Integer key : map.keySet()) {
            model.addElement(map.get(key).getItemName());
        }
        lstshop.setModel(model);
    }//GEN-LAST:event_btnbuyActionPerformed

    //uncompleted code for using your items
    private void btnitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnitemActionPerformed
        JOptionPane.showMessageDialog(this, "Not Completed");
    }//GEN-LAST:event_btnitemActionPerformed

    private void btnsellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsellActionPerformed
        //sets the shop type to sell
        shoptype = "sell";
        model.clear();
        //gets the players items
        temp = new HashMap<Integer, Item>();
        temp.putAll(p.getHashMap());
        //puts the items into the shop to sell
        for (Integer key : temp.keySet()) {
            model.addElement(temp.get(key).getItemName());
        }
        lstshop.setModel(model);
    }//GEN-LAST:event_btnsellActionPerformed

    //this took me forever to figure out, take a look at it becauses i'm proud ************************************************
    private void btnconfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnconfirmActionPerformed
        //try catch just incase no item has been selected
        try {
            //gets what item has been selected
            int loc = lstshop.getSelectedIndex();
            //player is buying the shops items
            if (shoptype.equals("buy")) {
                //checking if the player can afford the item, then buys it if possible
                if (p.getPlayerGold() >= map.get(loc + 1).getCost()) {
                    p.addItem(map.get(loc + 1));
                    p.addPlayerGold(map.get(loc + 1).getCost() * -1);
                }
                lstshop.setModel(model);
            }
            //player is selling their items
            if (shoptype.equals("sell")) {
                //since the key I associate with the items in the hashmap isn't 
                //reflective of it's position in the list, I have to check for
                //the amount of keys there are to get to it's position in the map
                int pass = 0;
                int tempint = -1;
                for (Integer key : temp.keySet()) {
                    if (pass == loc) {
                        tempint = key;
                    }
                    pass++;
                }
                //giving the player their gold from selling
                p.addPlayerGold(temp.get(tempint).getCost());
                p.removeItem(tempint);
                model.removeElementAt(loc);
            }
            
        } catch (Exception e) {
        };
    }//GEN-LAST:event_btnconfirmActionPerformed
    
    //getter for the enemy, enemies get harder each floor
    public void getEnemy() {
        int id = 0;
        //spawns different enemies at random per floor
        if (floor <= 10) {
            id = (int) ((Math.random() * 1 + 1) + 0.49);
        }
        if (floor > 10 && floor < 21) {
            id = (int) ((Math.random() * (5 - 3) + 3) + 0.49);
        }
        if (floor > 20 && floor < 31) {
            id = (int) ((Math.random() * (8 - 6) + 6) + 0.49);
        }
        //assigning id's to the enemies
        if (id == 1) {
            e = new Slime();
            lblenemy.setIcon(e.getImage());
            lblenemyname.setText(e.getEnemyName());
        }
        
        if (id == 2) {
            e = new Skeleton();
            lblenemy.setIcon(e.getImage());
            lblenemyname.setText(e.getEnemyName());
        }
        if (id == 3) {
            e = new SlimeQueen();
            lblenemy.setIcon(e.getImage());
            lblenemyname.setText(e.getEnemyName());
        }
        if (id == 4) {
            e = new IceGolem();
            lblenemy.setIcon(e.getImage());
            lblenemyname.setText(e.getEnemyName());
        }
        if (id == 5) {
            e = new BlackKnight();
            lblenemy.setIcon(e.getImage());
            lblenemyname.setText(e.getEnemyName());
        }
        
    }
    
    //the code that runs as the player fights the enemy
    public void battle(Enemy enemy) {
        //the enemy has a potential to get the first strike on you
        double firstStrike;
        firstStrike = Math.random() * 100 + 1;
        if (firstStrike > 75) {
            turn = 2;
            txtevent.append("The " + e.getEnemyName() + " gets the first strike!\n");
        } else {
            turn = 1;
            btnattack.setEnabled(true);
            btnitem.setEnabled(true);
        }
        //timertask to check various occurances throughout the battle
        battleCheck = new TimerTask() {
            @Override
            public void run() {
                //code that runs when an enemy is defeated
                if (e.getCurhealth() < 1) {
                    levelcheck = p.getPlayerLevel();
                    txtevent.append("\nThe " + e.getEnemyName() + " has perished!\n");
                    kills++;
                    txtevent.append(p.getPlayerName() + " gained " + e.getEnemyExpdrop() + " experience\n");
                    p.addPlayerExperience(e.getEnemyExpdrop());
                    txtevent.append(p.getPlayerName() + " gained " + e.getEnemyGpdrop() + " gold\n");
                    p.addPlayerGold(e.getEnemyGpdrop());
                    lblenemy.setIcon(null);
                    btnitem.setEnabled(true);
                    //makes it so you have to keep fighting till you beat ten floors
                    if (kills % 10 != 0) {
                        btnbattle.setText("Continue");
                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        btnbattle.setEnabled(true);
                        floor++;
                    }
                    //when ten enemies have been defeated that means you have fought
                    //through ten floors and you get the choice to continue or not
                    if (kills % 10 == 0) {
                        btnbattle.setText("Choose");
                        try {
                            Thread.sleep(750);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        btnbattle.setEnabled(true);
                    }
                    timer3.cancel();
                }
                
            }
        };
        //timers run that monitor the battle
        timer3 = new Timer();
        timer3.scheduleAtFixedRate(battleCheck, 200, 200);
        
        //the actual battle itself
        battleTimer = new TimerTask() {
            @Override
            public void run() {
                //runs this code if the enemy is alive
                if (e.getCurhealth() > 0) {
                    //the enemy attacks if it's their turn (2)
                    if (turn % 2 == 0) {
                        txtevent.append("\nThe " + e.getEnemyName() + " is attacking!\n");
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }
                        hit.start();
                        hit.setFramePosition(0);
                        //damage calulation for the enemy
                        int damage = ((int) ((Math.random() * (e.getEnemyAttack() - (e.getEnemyAttack() * 0.75)) + e.getEnemyAttack() * 0.75) + 0.50)) - p.getPlayerDefense();
                        //the enemy also hits at least one as well
                        if (damage < 1) {
                            damage = 1;
                        }
                        //telling the player what has happened
                        txtevent.append(p.getPlayerName() + " took " + damage + " damage!\n");
                        hpcheck = p.getPlayerCurrentHealth();
                        //applying damage to the player
                        p.addPlayerHealth((-1 * damage));
                        //turn goes to player
                        turn++;
                    } else {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                        }
                    }
                } else {
                    //timer doesn't run if the enemy is dead
                    timer2.cancel();
                }
                //makes it so the player can attack and use items if they are still alive
                if (p.getPlayerCurrentHealth() < hpcheck) {
                    btnattack.setEnabled(true);
                    btnitem.setEnabled(true);
                    hpcheck = p.getPlayerCurrentHealth();
                }
            }
        };
        //these timers run if the enemy is still alive
        if (e.getCurhealth() >= 1) {
            timer2 = new Timer();
            timer2.scheduleAtFixedRate(battleTimer, 200, 200);
        } else {
            timer2.cancel();
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel background;
    private javax.swing.JButton btnattack;
    private javax.swing.JButton btnbattle;
    private javax.swing.JButton btnbuy;
    private javax.swing.JButton btnconfirm;
    private javax.swing.JButton btnitem;
    private javax.swing.JButton btnsell;
    private javax.swing.JMenuItem devcons;
    private javax.swing.JProgressBar enemybar;
    private javax.swing.JProgressBar expbar;
    private javax.swing.JProgressBar hpbar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblenemy;
    private javax.swing.JLabel lblenemyname;
    private javax.swing.JLabel lblfloor;
    private javax.swing.JLabel lblgold;
    private javax.swing.JLabel lbllvl;
    private javax.swing.JList<String> lstshop;
    private javax.swing.JMenuItem mnuexit;
    private javax.swing.JMenuItem mnustats;
    private javax.swing.JTextArea txtevent;
    // End of variables declaration//GEN-END:variables
}
