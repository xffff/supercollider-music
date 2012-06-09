/************************************	*/
/* Ensemble Piece		             	*/
/*	Sequencer						*/
/************************************	*/

(
~stop_all = fork{
	~sequencer_stream.stop;
	Pdef.all.do(_.stop);
	~mOut.size.do{ |i| ~mOut[i].allNotesOff(0) };
};

~sequencer = Pseq([
	Pfuncn({"________________".postln; 0 }, 1),
//	Pfuncn({"Section 0".postln; 0 }, 1),
//	~sections[0],
//	Pfuncn({"Section 1".postln; 0 }, 1),
	~sections[1],
	Pfuncn({"Section 2".postln; 0 }, 1),
	~sections[2]
], 1);
)

