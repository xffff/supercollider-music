/************************************/
/* pp        		             	*/
/*	AUDIO						    */
/************************************/

(
~cleanup = nil;
~start_audio = nil;
~input = nil;
~fx = nil;
~pan = nil;
~output = nil;
~master_dry_bus = nil;
~master_fx_bus = nil;
~master_pan_bus = nil;
~buf_1 = nil;
~buf_2 = nil;
~dryaudio = nil;
~limiter = nil;

fork{
	~cleanup = {
		postln("Cleaning up");
		~input.free; ~fx.free; ~pan.free; ~output.free;
		~master_dry_bus.free; ~master_fx_bus.free; ~master_pan_bus.free;
		Buffer.freeAll;
		~dryaudio.free;
		~limiter.free;
		s.newAllocators;
		s.sync;
		postln("Done cleaning up");
	};
	~cleanup.fork;

	0.05.wait; // make sure we're cleaned up before we do the rest

	~start_audio = {
		(
			// groups
			~input = Group.new(s,\addToHead);	// from sampler
			~fx = Group.new(s, \addToTail); // fx chain
			~pan = Group.new(s, \addToTail); // from fx, split to surround
			~output = Group.new(s, \addToTail); // mix, limit, and output
			postln("Groups Allocated");

			// busses
			~master_dry_bus = Bus.audio(s,2);
			~master_fx_bus = Bus.audio(s,2);
			~master_pan_bus = Bus.audio(s,~numchans);
			postln("Busses Set");

			// buffers
			~buf_1 = Buffer.alloc(s, ~samplerate * 71, 2); // 2 channel 71 second buffer (this will be the longest it can be)
			~buf_2 = Buffer.alloc(s, ~samplerate * 71, 2);
			postln("Buffers Allocated");
		);

		////////////////////////////////////////////////////

		// start input
		(
			// dry audio
			~dryaudio = Synth(\dryaudio,
				[\amp, 0.85, \fxout, ~master_fx_bus, \dryout, ~master_dry_bus],
				target: ~input, addAction: \addToHead);
			~limiter = Synth(\limiter, [\amp,1, \fxin, ~master_fx_bus, \panin,
				~master_pan_bus, \dryin,~master_dry_bus, \out,0],
				target:~output,addAction:\addToTail);

			postln("Dry Audio On");
			s.queryAllNodes;
		);

	};
	~start_audio.fork;
};
)