package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.chess.gui.Table.MoveLog;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.Piece;

public class CapturedPiecesPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private final JPanel northPanel;
	private final JPanel southPanel;
	
	private static final Color PANEL_COLOR = Color.decode("#Fffeef");
	private static final Dimension CAPTURED_PIECES_DIMENSION = new Dimension(50, 80);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	
	public CapturedPiecesPanel() {
		super(new BorderLayout());
		setBackground(PANEL_COLOR);
		setBorder(PANEL_BORDER);
		this.northPanel = new JPanel(new GridLayout(8, 2));
		this.southPanel = new JPanel(new GridLayout(8, 2));
		this.northPanel.setBackground(PANEL_COLOR);
		this.southPanel.setBackground(PANEL_COLOR);
		this.add(this.northPanel, BorderLayout.NORTH);
		this.add(this.southPanel, BorderLayout.SOUTH);
		setPreferredSize(CAPTURED_PIECES_DIMENSION);
	}
	
	public void redo(MoveLog moveLog) {
		
		northPanel.removeAll();
		southPanel.removeAll();
		
		final List<Piece> capturedWhitePieces = new ArrayList<>();
		final List<Piece> capturedBlackPieces = new ArrayList<>();
		
		for (final Move move : moveLog.getMoves()) {
			if (move.isAttack()) {
				final Piece capturedPiece = move.getCapturedPiece();
				if (capturedPiece.getPieceAlliance().isWhite()) {
					capturedWhitePieces.add(capturedPiece);
				}
				else {
					capturedBlackPieces.add(capturedPiece);
				}
			}
		}
		
		Collections.sort(capturedWhitePieces, new Comparator<Piece>() {
			@Override
			public int compare(Piece o1, Piece o2) {
				return Math.max(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		
		Collections.sort(capturedBlackPieces, new Comparator<Piece>() {
			@Override
			public int compare(Piece o1, Piece o2) {
				return Math.max(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		
		for (final Piece capturedPiece : capturedWhitePieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("art/" + capturedPiece.getPieceAlliance().toString() 
						+ capturedPiece.toString() + ".png"));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel(icon); 
				this.northPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		for (final Piece capturedPiece : capturedBlackPieces) {
			try {
				final BufferedImage image = ImageIO.read(new File("art/" + capturedPiece.getPieceAlliance().toString() 
						+ capturedPiece.toString() + ".png"));
				final ImageIcon icon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel(icon);
				this.southPanel.add(imageLabel);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		validate();
	}
}
