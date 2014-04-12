/************************************	*/
/* Percussion Piece	             	*/
/*								*/
/************************************	*/
// SF IN  0-1 (FROM KONTAKT)
// SF OUT 2-15 // 0-1 DRY (FROM KONTAKT) // 2-3 DRY ()
// 

(
~stop_all = fork{
	~sequencer_stream.stop;
	Pdef.all.do(_.stop);
	~mOut.size.do{ |i| ~mOut[i].allNotesOff(0) };
};

~sequencer = Pseq([
	// maybe use pfuncn for section whatsits Pfuncn({"".postln; 1 }, 1)
	Pfuncn({"________________".postln; 1 }, 1),
	Pfuncn({"Section 1".postln; 1 }, 1),
	Ptpar([0,Pdef(\section1_drum1), 
		0, Pdef(\section1_fx1),
		0, Pdef(\section1_sy1)],1),
	Pfuncn({"Section 2".postln; 1 }, 1),
	Ptpar([0, Pdef(\section2_drum1),
		0.02, Pdef(\section2_fx1),
		0.02, Pdef(\section2_fx2), 
		0.02, Pdef(\section2_fx3)],1), 
	Pfuncn({"Section 3".postln; 1 }, 1),
	Ptpar([1, Pdef(\section3_drum1), 
		1.02, Pdef(\section3_drum2), 
		1.02, Pdef(\section3_drum3), 
		1.02, Pdef(\section3_fx1),
		1.02, Pdef(\section3_fx2),
		1.02, Pdef(\section3_sy1)],1),  
	Pfuncn({"Section 4".postln; 1 }, 1),
	Ptpar([2,Pdef(\section4_drum1),2,
		Pdef(\section4_fx1)],1),
	Pfuncn({"Section 5".postln; 1 }, 1),
	Ppar([Pdef(\section5_drum1),
		Pdef(\section5_fx1),
		Pdef(\section5_fx2),
		Pdef(\section5_fx3)],1),
	Pfuncn({"Section 6".postln; 1 }, 1),
	Pdef(\section6_drum1),
	Pfuncn({"Section 7".postln; 1 }, 1),
	Ptpar([0,Pdef(\section7_drum1),
		0,Pdef(\section7_drum2),
		0.02,Pdef(\section7_sy1),
		0.02,Pdef(\section7_fx1),
		0.02,Pdef(\section7_fx2),
		0.02,Pdef(\section7_fx3),
		0.02,Pdef(\section7_fx4)],1),
	Pfuncn({"Section 8".postln; 1 }, 1),
	Ppar([Pdef(\section8_drum1),
		Pdef(\section8_fx1),
		Pdef(\section8_fx2)],1),
	Pfuncn({"Section 9".postln; 1 }, 1),
	Pdef(\section9_drum1)
], 1);

//~sequencer.play;
)

