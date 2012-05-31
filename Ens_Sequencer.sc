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
	Pfuncn({"________________".postln; 1 }, 1),
], 1);
)

