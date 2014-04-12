/************************************	*/
/* Ensemble Piece		             	*/
/*	Sequencer						*/
/************************************	*/

(
~stop_all = {
	~sequencer_stream.stop;
};
~stop_all.fork;

~sequencer = Pseq([
	Pfuncn({"Section 1:".postln; 1},1),
	~sections[0],
	Pfuncn({"Section 2:".postln; 1},1),
	~sections[1],
	Pfuncn({"Section 3:".postln; 1},1),
	~sections[2],
	Pfuncn({"Section 4:".postln; 1},1),
	~sections[3],
	Pfuncn({"Section 5:".postln; 1},1),
	~sections[4],
	Pfuncn({"Section 6:".postln; 1},1),
	~sections[5],
	Pfuncn({"Section 7:".postln; 1},1),
	~sections[6],
	Pfuncn({"Section 8:".postln; 1},1),
	~sections[7],
	Pfuncn({"Section 9:".postln; 1},1),
	~sections[8],
	Pfuncn({"Section 10:".postln; 1},1),
	~sections[9],
	Pfuncn({"Section 11:".postln; 1},1),
	~sections[10],
	Pfuncn({"Section 12:".postln; 1},1),
	~sections[11],
	Pfuncn({"Section 13:".postln; 1},1),
	~sections[12],
	Pfuncn({"Section 14:".postln; 1},1),
	~sections[13],
	Pfuncn({"Section 15:".postln; 1},1),
	~sections[14]
], 1);


)
