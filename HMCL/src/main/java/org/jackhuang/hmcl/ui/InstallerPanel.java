/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2013  huangyuhui <huanghongxun2008@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.ui;

import org.jackhuang.hmcl.util.ui.Page;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jackhuang.hmcl.api.ui.TopTabPage;
import org.jackhuang.hmcl.util.C;
import org.jackhuang.hmcl.setting.Settings;
import org.jackhuang.hmcl.core.install.InstallerType;
import org.jackhuang.hmcl.core.install.InstallerVersionList;
import org.jackhuang.hmcl.util.task.TaskRunnable;
import org.jackhuang.hmcl.util.task.TaskWindow;
import org.jackhuang.hmcl.util.MessageBox;
import org.jackhuang.hmcl.util.StrUtils;
import org.jackhuang.hmcl.util.task.ProgressProviderListener;
import org.jackhuang.hmcl.util.task.Task;
import org.jackhuang.hmcl.util.ui.SwingUtils;

/**
 *
 * @author huangyuhui
 */
public class InstallerPanel extends Page implements ProgressProviderListener {

    GameSettingsPanel gsp;

    /**
     * Creates new form InstallerPanel
     *
     * @param gsp To get the minecraft version
     * @param installerType load which installer
     */
    public InstallerPanel(GameSettingsPanel gsp, InstallerType installerType) {
        initComponents();

        setOpaque(false);
        this.gsp = gsp;
        id = installerType;
        list = Settings.getInstance().getDownloadSource().getProvider().getInstallerByType(id);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnInstall = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        lstInstallers = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();

        btnInstall.setText(C.i18n("ui.button.install")); // NOI18N
        btnInstall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstallActionPerformed(evt);
            }
        });

        lstInstallers.setModel(SwingUtils.makeDefaultTableModel(new String[]{C.i18n("install.version"), C.i18n("install.mcversion")},
            new Class[]{String.class, String.class}, new boolean[]{false, false}));
    lstInstallers.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane12.setViewportView(lstInstallers);

    btnRefresh.setText(C.i18n("ui.button.refresh")); // NOI18N
    btnRefresh.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btnRefreshActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnInstall, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(jScrollPane12, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
        .addGroup(layout.createSequentialGroup()
            .addComponent(btnInstall)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(btnRefresh)
            .addGap(0, 0, Short.MAX_VALUE))
    );
    }// </editor-fold>//GEN-END:initComponents

    private void btnInstallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstallActionPerformed
        downloadSelectedRow();
    }//GEN-LAST:event_btnInstallActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshVersions();
    }//GEN-LAST:event_btnRefreshActionPerformed

    transient List<InstallerVersionList.InstallerVersion> versions;
    InstallerVersionList list;
    InstallerType id;

    void refreshVersions() {
        if (loading)
            return;
        Task t = list.refresh(new String[] { gsp.getMinecraftVersionFormatted() });
        if (t != null) {
            loading = true;
            DefaultTableModel model = SwingUtils.clearDefaultTable(lstInstallers);
            model.addRow(new Object[] { C.i18n("message.loading"), "", "" });
            t.with(new TaskRunnable(this::loadVersions)).setProgressProviderListener(this).runAsync();
        }
    }

    boolean loading = false;

    @Override
    public void setProgress(Task task, int prog, int max) {
        DefaultTableModel model = (DefaultTableModel) lstInstallers.getModel();
        if (model.getRowCount() > 0)
            model.setValueAt(C.i18n("message.loading") + " " + (prog < 0 ? "???" : Integer.toString(prog * 100 / max) + "%"), 0, 0);
    }

    @Override
    public void setStatus(Task task, String sta) {
    }

    @Override
    public void onProgressProviderDone(Task task) {
        loading = false;
        DefaultTableModel model = (DefaultTableModel) lstInstallers.getModel();
        if (model.getRowCount() > 0)
            model.removeRow(0);
    }

    public synchronized InstallerVersionList.InstallerVersion getVersion(int idx) {
        return versions.get(idx);
    }

    synchronized void downloadSelectedRow() {
        int idx = lstInstallers.getSelectedRow();
        if (versions == null || idx < 0 || idx >= versions.size()) {
            MessageBox.show(C.i18n("install.not_refreshed"));
            return;
        }
        TaskWindow.factory()
                .append(Settings.getLastProfile().service().install().download(Settings.getLastProfile().getSelectedVersion(), getVersion(idx), id))
                .append(new TaskRunnable(() -> gsp.refreshVersions()))
                .execute();
    }

    public void loadVersions() {
        SwingUtilities.invokeLater(() -> {
            synchronized (InstallerPanel.this) {
                DefaultTableModel model = (DefaultTableModel) lstInstallers.getModel();
                String mcver = StrUtils.formatVersion(gsp.getMinecraftVersionFormatted());
                versions = list.getVersions(mcver);
                SwingUtils.clearDefaultTable(lstInstallers);
                if (versions != null)
                    for (InstallerVersionList.InstallerVersion v : versions)
                        if (v != null)
                            model.addRow(new Object[] { v.selfVersion == null ? "null" : v.selfVersion, v.mcVersion == null ? "null" : v.mcVersion });
            }
        });
    }

    boolean refreshed = false;

    @Override
    public void onSelect(TopTabPage page) {
        super.onSelect(page);
        if (!refreshed) {
            refreshVersions();
            refreshed = true;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInstall;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JTable lstInstallers;
    // End of variables declaration//GEN-END:variables
}
